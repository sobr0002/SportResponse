package org.example.aiproject.service;

import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.model.RunningAnalysis;

public interface GroqClient {

    String aiResponse(String prompt);

    String buildPrompt(double distance, int minutes);

    RunningAnalysis extractAnalysis(RunningRequest request, String aiText);


}
