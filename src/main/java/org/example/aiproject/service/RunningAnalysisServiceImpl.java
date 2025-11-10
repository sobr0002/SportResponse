package org.example.aiproject.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.model.RunningAnalysis;
import org.example.aiproject.mapper.RunningMapper;
import org.example.aiproject.repository.RunningAnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunningAnalysisServiceImpl implements RunningAnalysisService {

    private final GroqClient groqClient;
    private final RunningAnalysisRepository repository;
    private final RunningMapper mapper;

    public RunningAnalysisServiceImpl(GroqClient groqClient, RunningAnalysisRepository repository, RunningMapper mapper) {
        this.groqClient = groqClient;
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- CREATE ---
    @Override
    public RunningResponse analyzeRun(RunningRequest request) {
        // Byg prompt baseret på distance og tid
        String prompt = buildPrompt(request.distance(), request.timeInMinutes());

        // Send til AI
        String aiText = groqClient.aiResponse(prompt);

        // Parse svaret og gem i databasen
        RunningAnalysis entity = extractAnalysis(request, aiText);
        RunningAnalysis saved = repository.save(entity);

        // Returnér DTO til frontend
        return mapper.toResponse(saved);
    }

    // --- READ ---

    @Override
    public List<RunningResponse> getRecentRuns() {
        List<RunningAnalysis> list = repository.findTop10ByOrderByCreatedAtDesc();
        return mapper.toResponseList(list);
    }

    @Override
    public RunningResponse getById(Long id) {

        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Kunne ikke finde analysen med id: " + id));
    }

    @Override
    public List<RunningResponse> findAll() {
        List<RunningAnalysis> list = repository.findAll();
        return mapper.toResponseList(list);
    }

    // --- DELETE ---

    @Override
    public void deleteRunningAnalysis(Long id) {

        RunningAnalysis analysis = repository.findById(id).orElseThrow(() -> new RuntimeException("Kunne ikke finde analysen med id " + id));

        repository.deleteById(analysis.getId());
    }

    // --- Hjælpemetoder ---

    // Bygger prompt som sendes til Groq AI
    private String buildPrompt(double distance, int minutes) {
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
                "half_marathon_pace": "Pace i min/km for halvmarathon (typisk 10-15 sek/km hurtigere end nuværende)",
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
            2. Estimer halvmarathon/marathon tider baseret på den givne pace.
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

    private static RunningAnalysis extractAnalysis(RunningRequest request, String aiText) {
        try {
            // Fjern markdown code blocks hvis de findes
            String cleanedJson = aiText
                    .replaceAll("```json\\s*", "")   // Fjern ```jason
                    .replaceAll("```\\s*","")   // Fjern ```
                    .trim();

            cleanedJson = cleanedJson.replaceAll("\\n", "\n");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(cleanedJson); // Parse cleaned JSON

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