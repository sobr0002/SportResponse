package org.example.aiproject.service;

import org.example.aiproject.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }


}
