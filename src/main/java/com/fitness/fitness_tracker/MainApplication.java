package com.fitness.fitness_tracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.URL;

@SpringBootApplication
public class MainApplication extends Application {

    private static Stage stage;
    public static ConfigurableApplicationContext APPLICATION_CONTEXT;

    @Override
    public void init() throws Exception {
        APPLICATION_CONTEXT = SpringApplication.run(MainApplication.class);
        super.init();
    }

    @Override
    public void stop() throws Exception {
        APPLICATION_CONTEXT.close();
        super.stop();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        MainApplication.stage = stage;
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Fitness Tracker");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Easy way of changing scene from everywhere in the software.
     * @param fxml address to the scene file (e.g. "main-view.fxml")
     * @throws IOException
     */
    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(MainApplication.class.getResource(fxml));
        stage.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch(MainApplication.class, args);
    }
}