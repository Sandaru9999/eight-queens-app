package com.eightqueens.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EightQueensUI {

    public static void open() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader =
                    new FXMLLoader(EightQueensUI.class.getResource("eight-queens-view.fxml"));

            Scene scene = new Scene(loader.load());
            stage.setTitle("Eight Queens Puzzle");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
