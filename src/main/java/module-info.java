module com.fitness.fitness_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires org.hibernate.orm.core;
    requires spring.core;
    requires spring.beans;

    exports com.fitness.fitness_tracker.converters;
    exports com.fitness.fitness_tracker.models;
    exports com.fitness.fitness_tracker.repositories;
    exports com.fitness.fitness_tracker.services;
    opens com.fitness.fitness_tracker.services;
    exports com.fitness.fitness_tracker;
    opens com.fitness.fitness_tracker;
    opens com.fitness.fitness_tracker.models;
}