package org.example.aiproject.controller;


import org.example.aiproject.dto.RunningResponse;
import org.example.aiproject.dto.RunningRequest;
import org.example.aiproject.service.RunningAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/running")
@CrossOrigin(origins = "*")
public class RunningController {

    private final RunningAnalysisService service;

    public RunningController(RunningAnalysisService service) {
        this.service = service;
    }

    // Analyser et løb (distance + tid) via AI og returnér resultatet
    @PostMapping("/analyze") // var tom før
    public ResponseEntity<RunningResponse> analyze(@RequestBody RunningRequest request) { // @Valid
        RunningResponse response = service.analyzeRun(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RunningResponse>> getAll() {

        List<RunningResponse> responseList = service.findAll();

        return ResponseEntity.ok(responseList);
    }

    // Hent de seneste 10 analyser til historik
    @GetMapping("/recent")
    public ResponseEntity<List<RunningResponse>> recent() {

        List<RunningResponse> recentResponseList = service.getRecentRuns();
        return ResponseEntity.ok(recentResponseList);
    }

    // Hent en enkelt analyse pr id
    @GetMapping("/{id}")
    public ResponseEntity<RunningResponse> getById(@PathVariable Long id) {

        RunningResponse response = service.getById(id);

        return ResponseEntity.ok(response);

    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        service.deleteRunningAnalysis(id);
        return ResponseEntity.noContent().build(); // HTTP 204 = anmodning behandlet uden fejl
    }
}
