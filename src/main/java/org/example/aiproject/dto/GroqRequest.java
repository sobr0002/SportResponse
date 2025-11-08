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

    /*
    Nested record for chat messages.
    Nested fordi:
     - Message er en integreret del af GroqRequest og giver kun mening i denne kontekst
     - Spejler JSON strukturen hvor messages er et array af objekter inde i request body
     - Undgår navnekonflikter med andre Message typer i projektet
     */
    public record Message(
            String role,    // om det er "llm" eller "bruger"
            String content  // beskedens indhold
    ) {}
}
