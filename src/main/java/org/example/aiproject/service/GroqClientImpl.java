package org.example.aiproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.model.RunningAnalysis;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.List;

@Service
public class GroqClientImpl implements GroqClient {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String openApiKey; // Nøglen skal ligge i vores environment variables


    // Injicerer WebClient i konstruktør - bygger webclient-objekt
    public GroqClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.groq.com/openai/v1/chat/completions").build();
    }

    @Override
    public String aiResponse(String prompt) {
        try {
            // --- Bygger JSON-request til Groq = Serialisering
            Map<String, Object> requestBody = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "temperature", 0.7,
                    "max_tokens", 512,
                    "messages", List.of(
                            Map.of("role", "system", "content", "Du er en erfaren løbetræner, der analyserer løb og giver konkrete råd."),
                            Map.of("role", "user", "content", prompt)
                    )
            );

            // --- Sender request til Groq API

            Map<String, Object> response = webClient.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> headers.setBearerAuth(openApiKey))
                    .bodyValue(requestBody)// sender JSON-data
                    .retrieve() // Henter response
                    .bodyToMono(Map.class) // Deserialiserer JSON til Running-objekt
                    .block(); // Venter på svar fra webclient før resten af koden køres

            if (response == null) {
                return "Intet svar fra Groq API.";
            }

            // Henter LLM'ens tekstsvar: choices[0].message.content
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices == null || choices.isEmpty()) {
                return "Groq returnerede ingen choices.";
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            if (message == null) {
                return "Groq svar mangler message-content.";
            }

            Object content = message.get("content");
            return content != null ? content.toString().trim() : "Groq content er tomt.";

        } catch (Exception e) {
            return "Fejl ved kontakt til Groq: " + e.getMessage();
        }
    }


    // Bygger prompt som sendes til Groq AI
    public String buildPrompt(double distance, int minutes) {
        // Beregn korrekte værdier på forhånd
        double hours = minutes / 60.0;
        double pace = minutes / distance;   // min/km
        double speed = distance / hours;    // km/t

        return String.format("""
            VIGTIG: Brug NØJAGTIG matematik baseret på disse data:
            
            Distance: %.2f km
            Tid: %d minutter (= %.2f timer)
            
            BEREGN PRÆCIST:
            - Pace = %d ÷ %.2f = %.2f min/km
            - Hastighed = %.2f ÷ %.2f = %.2f km/t
            
            Analyser løbet og svar KUN med valid JSON i dette format:
            {
              "analysis": {
                "pace": "%.2f min/km",
                "speed": "%.2f km/t",
                "summary": "Kort vurdering af tempoet (fx roligt, moderat, hurtigt baseret på pace %.2f min/km)"
              },
              "estimates": {
                "half_marathon_time": "Estimeret tid for 21.1 km baseret på pace %.2f min/km (fx 1t 45m)",
                "half_marathon_pace": "Pace i min/km for halvmaraton (typisk 10-15 sek/km hurtigere end nuværende)",
                "marathon_time": "Estimeret tid for 42.2 km baseret på pace %.2f min/km",
                "marathon_pace": "Pace i min/km for marathon (typisk 20-30 sek/km langsommere end nuværende)"
              },
              "training_suggestions": [
                {
                  "type": "Intervalløb",
                  "description": "Konkret beskrivelse"
                },
                {
                  "type": "Roligt løb",
                  "description": "Konkret beskrivelse"
                },
                {
                  "type": "Tempoløb",
                  "description": "Konkret beskrivelse"
                }
              ]
            }
            
            VIGTIGT:
            - Lav PRÆCIS 3 separate objekter - ÉT pr. træningstype
            - Brug IKKE linjeskift i description felterne
            - Hold description kort (max 100 tegn pr. beskrivelse)
            
            Retningslinjer:
            1. BRUG DE BEREGNEDE VÆRDIER OVENFOR - lav IKKE dine egne beregninger.
            2. Estimer halvmaraton/maratontider baseret på den givne pace.
            3. Hvis løbet er under 3 km, nævn at estimatet kan være usikkert.
            4. Angiv pace med KOLON (fx 3:50 min/km), IKKE komma eller punktum.
            5. Angiv hastighed i km/t med komma som decimalseparator (fx 17,14 km/t).
            6. Hold descriptions korte (max 100 tegn) UDEN linjeskift.
            7. Svar KUN med valid JSON — ingen tekst udenfor JSON-strukturen.
            8. Brug de beregnede tal: %.2f min/km og %.2f km/t
            """,

                distance, minutes, hours,           // Linje 1-3: Input data
                minutes, distance, pace,            // Linje 5: Pace beregning
                distance, hours, speed,             // Linje 6: Speed beregning
                pace, speed, pace,                  // analysis section
                pace,                               // half_marathon_time
                pace,                               // marathon_time
                pace, speed                         // Gentag i slut-reminder
        );
    }

    // For at udlede data fra AI-svaret, bruges JsonNode-træet = analysis/suggestions
    public RunningAnalysis extractAnalysis(RunningRequest request, String aiText) {
        try {
            // Fjern markdown code blocks hvis de findes
            String cleanedJson = aiText
                    .replaceAll("```json\\s*", "")   // Fjern ```json
                    .replaceAll("```\\s*","")   // Fjern ```
                    .trim();

            // For rigtig linjeskift. AI JSON -> Java
            cleanedJson = cleanedJson.replaceAll("\\n", "\n");

            // Parser teksten til JsonNode-træ
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(cleanedJson); // Parse cleaned JSON

            // Her bruges hjælpemetoder
            String analysis = formatAnalysisAndEstimates(root);
            String suggestions = formatTrainingSuggestions(root);

            RunningAnalysis entity = new RunningAnalysis();
            entity.setDistance(request.distance());
            entity.setTimeInMinutes(request.timeInMinutes());
            entity.setAnalysis(analysis);
            entity.setSuggestions(suggestions);
            return entity;

        } catch (Exception e) {
            return createFallbackEntity(request, aiText, e);
        }
    }

    // --- Hjælpemetoder ---

    // Static fordi det er hjælpemetoder
    private static String formatAnalysisAndEstimates(JsonNode root) {
        JsonNode analysisNode = root.path("analysis");
        String pace = analysisNode.path("pace").asText();
        String speed = analysisNode.path("speed").asText();
        String summary = analysisNode.path("summary").asText();

        JsonNode estimatesNode = root.path("estimates");
        String halfMarathon = estimatesNode.path("half_marathon_time").asText();
        String marathon = estimatesNode.path("marathon_time").asText();

        return String.format("%s, %s\n%s\n\nHalvmarathon: %s\nMarathon: %s",
                pace, speed, summary, halfMarathon, marathon);
    }

    private static String formatTrainingSuggestions(JsonNode root) {
        JsonNode suggestionsArray = root.path("training_suggestions");
        StringBuilder sb = new StringBuilder();

        for (JsonNode suggestion : suggestionsArray) {
            String type = suggestion.path("type").asText();
            String description = suggestion.path("description").asText();
            sb.append(type).append(": ").append(description).append("\n\n");
        }

        return sb.toString().trim();
    }

    // Hvis suggestions ikke kan udledes, får brugeren kun en analyse
    private static RunningAnalysis createFallbackEntity(RunningRequest request, String aiText, Exception e) {
        System.err.println("Fejl ved parsing af AI svar: " + e.getMessage());
        RunningAnalysis entity = new RunningAnalysis();
        entity.setDistance(request.distance());
        entity.setTimeInMinutes(request.timeInMinutes());
        entity.setAnalysis(aiText);
        entity.setSuggestions("Kunne ikke parse træningsforslag");
        return entity;
    }
}

