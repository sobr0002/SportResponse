package org.example.aiproject.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    private String aiResponse;

    // Token tracking

    @Column(name = "prompt_tokens")
    private int promptTokens; // beskriv hvad det er

    @Column(name = "completion_tokens")
    private int completionTokens; // beskriv hvad det er

    @Column(name = "total_tokens")
    private int totalTokens; // beskriv hvad det er

    @CreationTimestamp // skriv en kommentar omkring denne annotering
    @Column(name = "created")
    private LocalDateTime created;

    // Konstruktør

    public Workout() {

    }

    public Workout(Long id, String description, String aiResponse,
                   int promptTokens, int completionTokens, int totalTokens) {
        this.id = id;
        this.description = description;
        this.aiResponse = aiResponse;
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAiResponse() {
        return aiResponse;
    }
    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    public int getPromptTokens() {
        return promptTokens;
    }
    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }
    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalTokens() {
        return totalTokens;
    }
    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public LocalDateTime getCreated() {
        return created;
    }
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    // toString

    @Override
    public String toString() {
        return "Workout{" +
                "created=" + created +
                ", totalTokens=" + totalTokens +
                ", completionTokens=" + completionTokens +
                ", promptTokens=" + promptTokens +
                ", aiResponse='" + aiResponse + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}
