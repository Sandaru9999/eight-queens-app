package com.gamemenu;

import com.eightqueens.ui.MenuScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameMenu extends Application {

    @Override
    public void start(Stage stage) {
        // Open MenuScreen in full-screen
        MenuScreen.open(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
