package org.example.aiproject.dto;

/*
DTO til running request fra frontend
Validerer input og sikrer data integritet
 */

public record RunningRequest(
        double distance,    // km
        int timeInMinutes   // minutter {
) {
        // Validering af variabler
    public RunningRequest {
            if(distance <=0) {
        throw new IllegalArgumentException("Distance skal være positiv");
        }
        if(timeInMinutes <=0) {
        throw new IllegalArgumentException("Tid skal være positiv");
        }
    }
}
