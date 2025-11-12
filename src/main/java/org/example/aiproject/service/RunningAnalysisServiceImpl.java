package org.example.aiproject.service;

import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.model.RunningAnalysis;
import org.example.aiproject.mapper.RunningMapper;
import org.example.aiproject.repository.RunningAnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunningAnalysisServiceImpl implements RunningAnalysisService {

    private final GroqClient groqClient;
    private final RunningAnalysisRepository repository;
    private final RunningMapper mapper;

    public RunningAnalysisServiceImpl(GroqClient groqClient, RunningAnalysisRepository repository, RunningMapper mapper) {
        this.groqClient = groqClient;
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- CREATE ---
    @Override
    public RunningResponse analyzeRun(RunningRequest request) {
        // Beregn pace og speed
        double pace = request.timeInMinutes() / request.distance();
        double speed = request.distance() / (request.timeInMinutes() / 60.0);
        String paceFormatted = formatPace(pace); // "5:00 min/km"

        // Byg prompt baseret på distance og tid
        String prompt = groqClient.buildPrompt(request.distance(), request.timeInMinutes());

        // Send til AI
        String aiText = groqClient.aiResponse(prompt);

        // Parse svaret og gem i databasen
        RunningAnalysis entity = groqClient.extractAnalysis(request, aiText);
        entity.setPace(pace);
        entity.setSpeed(speed);
        entity.setPaceFormatted(paceFormatted);
        RunningAnalysis saved = repository.save(entity);

        // Returnér DTO til frontend
        return mapper.toResponse(saved);
    }

    // -- Hjælpemetode --
    private String formatPace(double pace) {
        int minutes = (int) pace;
        int seconds = (int) Math.round((pace - minutes) * 60);
        return String.format("%d:%02d min/km", minutes, seconds);
    }

    // --- READ ---

    @Override
    public List<RunningResponse> getRecentRuns() {
        List<RunningAnalysis> list = repository.findTop10ByOrderByCreatedAtDesc();
        return mapper.toResponseList(list);
    }

    @Override
    public RunningResponse getById(Long id) {

        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Kunne ikke finde analysen med id: " + id));
    }

    @Override
    public List<RunningResponse> findAll() {
        List<RunningAnalysis> list = repository.findAll();
        return mapper.toResponseList(list);
    }

    // --- DELETE ---

    @Override
    public void deleteRunningAnalysis(Long id) {

        RunningAnalysis analysis = repository.findById(id).orElseThrow(() -> new RuntimeException("Kunne ikke finde analysen med id " + id));

        repository.deleteById(analysis.getId());
    }
}