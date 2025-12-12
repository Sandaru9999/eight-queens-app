package com.eightqueens;

import com.eightqueens.ui.MenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create MenuScreen
        MenuScreen menu = new MenuScreen(stage);

        // Create Scene
        Scene scene = new Scene(menu.getView(), 600, 400);

        // Configure Stage
        stage.setTitle("Game Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
