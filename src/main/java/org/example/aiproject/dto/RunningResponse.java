package org.example.aiproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/*
DTO til running responses til frontend
Indeholder alle relevante informationer for brugeren
 */

public record RunningResponse(
        Long id,
        double distance,        // km i løbet (fra request)
        int timeInMinutes,      // tid i minutter (fra request)
        String analysis,        // AI's analyse: pace, km/t, projections
        String suggestions,     // Ai's 3 træningsforslag

        @JsonFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss") // Formaterer til f.eks 01-01-2025 00:00:00
        LocalDateTime createdAt
) {}
