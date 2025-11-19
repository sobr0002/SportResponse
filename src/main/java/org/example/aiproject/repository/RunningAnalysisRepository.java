package org.example.aiproject.repository;


import org.example.aiproject.model.RunningAnalysis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningAnalysisRepository extends JpaRepository<RunningAnalysis, Long> {

    // Spring Data genererer automatisk:
    // - save(RunningAnalysis entity)
    // - findById(Long id)
    // - findAll()
    // - deleteById(Long id)

    // --- NYTTIGE CUSTOM QUERIES ---

    // Hent de seneste 10 løb (til historik på frontend)
    List<RunningAnalysis> findTop10ByOrderByCreatedAtDesc();

    // Hent alle løb fra en bestemt dato (fx sidste måned)
    // List<RunningAnalysis> findByCreatedAtAfter(LocalDateTime date);

    // Find løb over en bestemt distance
    // List<RunningAnalysis> findByDistanceGreaterThanEqual(double minDistance);

    // Find hurtigste løb (laveste tid for given distance)
    // List<RunningAnalysis> findByDistanceOrderByTimeInMinutesAsc(double distance);

}
