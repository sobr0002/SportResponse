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

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt
) {}
