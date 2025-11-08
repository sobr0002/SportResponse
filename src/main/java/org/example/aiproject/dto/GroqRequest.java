package org.example.aiproject.dto;

import java.util.List;

/*
DTO til requests sendt til Groq API | Backend -> Groq API
Matcher Groq's API spec præcist
 */

public record GroqRequest(
        String model,
        List<Message> messages,
        double temperatur,
        int max_tokens
) {

    // Nested record for chat messages.
    public record Message(
            String role,    // "bruger"
            String content  // Beskedens indhold
    ) {}
}
