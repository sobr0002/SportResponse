package org.example.aiproject.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "running_analysis")
public class RunningAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double distance;              // km løbet

    @Column(nullable = false)
    private int timeInMinutes;            // tid i minutter

    @Column(length = 1000)
    private String analysis;              // AI's analyse (pace, km/t, projections)

    @Column(length = 2000)
    private String suggestions;           // AI's 3 træningsforslag

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;      // Hvornår blev analysen lavet

    public RunningAnalysis() {}

    public RunningAnalysis(Long id, double distance, int timeInMinutes, String analysis, String suggestions, LocalDateTime createdAt) {
        this.id = id;
        this.distance = distance;
        this.timeInMinutes = timeInMinutes;
        this.analysis = analysis;
        this.suggestions = suggestions;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RunningAnalysis{" +
                "id=" + id +
                ", distance=" + distance +
                ", timeInMinutes=" + timeInMinutes +
                ", createdAt=" + createdAt +
                '}';
    }

}
