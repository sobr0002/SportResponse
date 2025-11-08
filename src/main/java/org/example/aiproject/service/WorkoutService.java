package org.example.aiproject.service;

import org.example.aiproject.model.Workout;
import org.example.aiproject.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService implements IWorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }


}
