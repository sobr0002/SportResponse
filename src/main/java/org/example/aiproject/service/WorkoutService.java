package org.example.aiproject.service;

import org.example.aiproject.model.Workout;
import org.example.aiproject.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }
}
