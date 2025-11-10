package org.example.aiproject.service;

import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.model.RunningAnalysis;

import java.util.List;
import java.util.Optional;


public interface RunningAnalysisService {

    // Opret og gem en analyse via LLM (create)
    RunningResponse analyzeRun(RunningRequest request);

    // Read metoder
    List<RunningResponse> getRecentRuns(); // Hent historie

    RunningResponse getById(Long id);     // Hent enkelt løb

    List<RunningResponse> findAll();     // Hent alle løb



}