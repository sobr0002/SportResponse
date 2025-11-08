package org.example.aiproject.dto;

/*
DTO til workout request fra frontend
Valider input og sikrer data integritet
 */

public record WorkoutRequest(String description) {

    public WorkoutRequest {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Beskrivelse må ikke være tom");
        }
        // Kan tilføje håndtering af beskrivelsens længde (max length 500)

        // Trim whitespaces
        description = description.trim(); // Kan sænke token forbruget en smule
    }
}
