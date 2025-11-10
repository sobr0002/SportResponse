package org.example.aiproject.service;

import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.model.RunningAnalysis;
import org.example.aiproject.mapper.RunningMapper;
import org.example.aiproject.repository.RunningAnalysisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // Byg prompt baseret på distance og tid
        String prompt = buildPrompt(request.distance(), request.timeInMinutes());

        // Send til AI
        String aiText = groqClient.aiResponse(prompt);

        // Parse svaret og gem i databasen
        RunningAnalysis entity = extractAnalysis(request, aiText);
        RunningAnalysis saved = repository.save(entity);

        // Returnér DTO til frontend
        return mapper.toResponse(saved);
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

    // --- Hjælpemetoder ---

    // Bygger prompt som sendes til Groq AI
    private String buildPrompt(double distance, int minutes) {
        return String.format("""
            Brugeren løb %.2f kilometer på %d minutter.

            Analyser løbet ud fra distance og tid, og svar KUN med valid JSON i dette format:
            {
              "analysis": {
                "pace": "Pace i min/km",
                "speed": "Hastighed i km/t",
                "summary": "Kort vurdering af tempoet (fx roligt, moderat, hurtigt)"
              },
              "estimates": {
                "half_marathon_time": "Estimeret tid for 21.1 km (fx 1t 45m)",
                "half_marathon_pace": "Pace i min/km for halvmarathon",
                "marathon_time": "Estimeret tid for 42.2 km",
                "marathon_pace": "Pace i min/km for marathon"
              },
              "training_suggestions": [
                {
                  "type": "Træningstype (fx Roligt løb)",
                  "description": "Kort beskrivelse af formålet med træningen"
                }
              ]
            }
    
            Retningslinjer:
            1. Brug brugerens distance og tid til realistiske estimater.
            2. Hvis løbet er under 3 km, skriv at estimatet kan være usikkert.
            3. Angiv alle værdier i dansk format (min/km, km/t).
            4. Svar kun med JSON — ingen forklarende tekst udenfor JSON-strukturen.
            """, distance, minutes);
    }

    // Simpel parsing af AI's svar for at adskille analyse og suggestion
    private static RunningAnalysis extractAnalysis(RunningRequest request, String aiText) {
        String analysisText = aiText;
        String suggestionText = "";

        if (aiText.toLowerCase().contains("forslag")) {
            String[] parts = aiText.split("(?i)forslag");
            analysisText = parts[0].trim();
            if (parts.length > 1) {
                suggestionText = "Forslag" + parts[1].trim();
            }
        }

        RunningAnalysis entity = new RunningAnalysis();
        entity.setDistance(request.distance());
        entity.setTimeInMinutes(request.timeInMinutes());
        entity.setAnalysis(analysisText);
        entity.setSuggestions(suggestionText);
        return entity;
    }
}