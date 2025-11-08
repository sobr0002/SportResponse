package org.example.aiproject.dto;

/*
DTO til responses fra Groq API | Grop API -> Backend
Matcher Grop's response format <-- måske mere forklarende
 */

import java.util.List;

public record GroqResponse(
        String id,
        String object,
        long created,
        String model,
        List<Choice> choices,
        Usage usage
) {

    // Nested record for AI valg
    public record Choice(
            int index,
            Message message,
            String finish_reason
    ) {}

    // Nested record for chat messages
    public record Message(
            String role,
            String content
    ) {}

    // Nested record for token usage tracking
    public record Usage(
            int prompt_tokens,
            int completion_tokens,
            int total_tokens
    ) {}
}
