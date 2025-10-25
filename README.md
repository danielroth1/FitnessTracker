# FitnessTracker

FitnessTracker is a small Java desktop application built with JavaFX and Maven. It provides a lightweight UI for managing workout routines. The project is structured as a standard Maven project and includes a simple model, repository, service layer, and JavaFX views/controllers.

<img src="https://github.com/user-attachments/assets/12849167-266c-49c5-9e80-2a6d158cdf82" width="500"/>

## Features

- Manage workout routines (model: `WorkoutRoutine`).
- JavaFX UI with views located in `src/main/resources/com/fitness/fitness_tracker`.
- Conversion utilities (e.g., `DurationConverter`).
- Simple repository and service layers for data handling.

## Project structure

Key files and directories:

- `pom.xml` - Maven build configuration.
- `mvnw`, `mvnw.cmd` - Maven wrapper for Unix/Windows respectively.
- `src/main/java` - Java source files.
  - `com.fitness.fitness_tracker` - main application classes and controllers:
    - `FitnessTrackerApplication.java`, `MainApplication.java`, `MainViewController.java`, `WorkoutRoutineViewController.java`
    - `models/WorkoutRoutine.java` - data model.
    - `repositories/WorkoutRoutineRepository.java` - data persistence layer.
    - `services/WorkoutRoutineService.java` - business logic.
    - `converters/DurationConverter.java` - helper for converting durations.
    - `views/WorkoutRoutinePopup.java`, `GridPaneDragAndDropHandler.java` - UI helper classes.
- `src/main/resources` - resources including FXML files for views:
  - `com/fitness/fitness_tracker/main-view.fxml`
  - `com/fitness/fitness_tracker/workout-routines-view.fxml`
- `src/test` - unit tests.

## Requirements

- JDK 17 or later (project contains compiled classes targeting modern Java; build tools may rely on Java 17+). Confirm with `java -version`.
- Maven (the project includes the Maven wrapper so you don't need a global Maven install).

## Build

Open a Windows Command Prompt in the project root (`d:\projects\FitnessTracker`) and run:

```
mvnw.cmd clean package
```

This will compile the project and create a JAR in the `target` directory.

## Run

Run the JavaFX application using the Maven exec plugin (if configured) or by running the packaged JAR. Example using the Maven wrapper:

```
mvnw.cmd javafx:run
```

If you prefer running the JAR produced by `package`, use:

```
java -jar target\your-artifact-name.jar
```

Replace `your-artifact-name.jar` with the actual JAR name created by Maven.

## Tests

Run tests with:

```
mvnw.cmd test
```

## Notes for developers

- FXML views are in `src/main/resources/com/fitness/fitness_tracker` and are loaded by controllers in `src/main/java/com/fitness/fitness_tracker`.
- The project uses a repository/service pattern. For small demos, repository implementations may be in-memory.
- If you change JavaFX versions, ensure the `pom.xml` dependencies and module info (`module-info.java`) remain consistent.

## Troubleshooting

- If you see module-related errors, check `module-info.java` and ensure JavaFX modules are listed and provided at runtime.
- On Windows, remember to use `mvnw.cmd` instead of `mvnw`.

