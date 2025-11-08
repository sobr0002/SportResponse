package org.example.aiproject.dto;

import java.util.List;

/*
DTO til requests sendt til Groq API | Backend -> Groq API
Matcher Groq's API spec præcist
 */

public record GroqRequest(
        String model, // modellen vi vælger at bruge
        List<Message> messages, // liste af beskeden mellem llm og bruger
        double temperature, // styrer llm'ens kreativitet - 0.0 til 2.0
        int max_tokens // bestemmer længden på svaret fra llm
) {

    // Nested record for chat messages.
    public record Message(
            String role,    // om det er "llm" eller "bruger"
            String content  // beskedens indhold
    ) {}
}
