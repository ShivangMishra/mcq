package com.exam.mcq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class McqExamApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(McqExamApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        McqExamController controller = fxmlLoader.getController();
        controller.start();
        stage.setTitle("MCQ Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}