package org.example.aiproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/*
DTO til workout responses til frontend
Indeholder alle relevante informationer for brugeren
 */

public record WorkoutResponse(
        Long id,
        String description,
        String aiResponse,
        int totalTokens,

        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss") // Formatere til fx. 01-01-2025 00:00:00
        LocalDateTime created
) {}
