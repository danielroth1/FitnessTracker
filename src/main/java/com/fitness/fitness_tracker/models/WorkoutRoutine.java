package com.fitness.fitness_tracker.models;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class WorkoutRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate; // when does the workout start

    @Column(columnDefinition = "TIMESTAMP", nullable = true)
    private LocalDateTime endDate; // time the workout takes

    @Column(nullable = true)
    private Duration repeatDuration; // workout repeats every x

    public WorkoutRoutine() {
    }

    public WorkoutRoutine(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Duration getRepeatDuration() {
        return this.repeatDuration;
    }
    public void setRepeatDuration(Duration repeatDuration) {
        this.repeatDuration = repeatDuration;
    }

}
