package org.example.aiproject.service;

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
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class) // Deserialiserer JSON til Running-objekt
                    .block();

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
}

