package com.fitness.fitness_tracker.services;

import com.fitness.fitness_tracker.models.WorkoutRoutine;
import com.fitness.fitness_tracker.repositories.WorkoutRoutineRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutRoutineService {

    private final WorkoutRoutineRepository workoutRoutineRepository;

    public WorkoutRoutineService(WorkoutRoutineRepository workoutRoutineRepository) {
        this.workoutRoutineRepository = workoutRoutineRepository;
    }

    public List<WorkoutRoutine> getWorkoutRoutines() {
        return this.workoutRoutineRepository.findAll();
    }

    public void addOrUpdateWorkoutRoutine(WorkoutRoutine routine) {
        this.workoutRoutineRepository.save(routine);
    }

    public void removeWorkoutRoutine(WorkoutRoutine workoutRoutine) {
        this.workoutRoutineRepository.delete(workoutRoutine);
    }
}
