package com.eightqueens.ui;

import com.gamemenu.GameMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MenuScreen {

    private StackPane rootWrapper;
    private VBox panel;
    private Stage stage;
    private GameMenuController controller;

    public MenuScreen(Stage stage) {
        this.stage = stage;
        this.controller = new GameMenuController(stage);

        rootWrapper = new StackPane();
        rootWrapper.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #1f1c2c, #928DAB);"
        );

        panel = new VBox(25);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(40));
        panel.setMaxWidth(450);

        panel.setBackground(new Background(new BackgroundFill(
                Color.rgb(255, 255, 255, 0.12),
                new CornerRadii(20),
                Insets.EMPTY
        )));

        BoxBlur blur = new BoxBlur(10, 10, 3);
        panel.setEffect(blur);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(20);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.35));
        panel.setEffect(shadow);

        Button title = new Button("🎮 GAME HUB");
        title.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.WHITE);
        title.setDisable(true);
        title.setStyle("-fx-background-color: transparent;");

        Button btnEightQueens = createGameButton("♛ Eight Queens Puzzle");
        Button btnTowerOfHanoi = createGameButton("🗼 Tower of Hanoi");
        Button btnTSP = createGameButton("🛣️ Traveling Salesman Problem");
        Button btnSnakeLadder = createGameButton("🎲 Snake & Ladder");
        Button btnTraffic = createGameButton("🚦 Traffic Simulation");

        btnEightQueens.setOnAction(controller::onEightQueensClick);
        btnTowerOfHanoi.setOnAction(controller::onTowerOfHanoiClick);
        btnTSP.setOnAction(controller::onGame3Click);
        btnSnakeLadder.setOnAction(controller::onGame4Click);
        btnTraffic.setOnAction(controller::onGame5Click);

        panel.getChildren().addAll(
                title,
                btnEightQueens,
                btnTowerOfHanoi,
                btnTSP,
                btnSnakeLadder,
                btnTraffic
        );

        rootWrapper.getChildren().add(panel);
    }

    private Button createGameButton(String text) {
        Button btn = new Button(text);

        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 20));
        btn.setPrefWidth(320);
        btn.setPrefHeight(55);
        btn.setTextFill(Color.WHITE);

        btn.setStyle(
                "-fx-background-radius: 25;" +
                "-fx-background-color: linear-gradient(to right, #4776E6, #8E54E9);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 15, 0.2, 0, 4);"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-background-radius: 25;" +
                                "-fx-background-color: linear-gradient(to right, #8E54E9, #4776E6);" +
                                "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.55), 20, 0.3, 0, 0);"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-background-radius: 25;" +
                                "-fx-background-color: linear-gradient(to right, #4776E6, #8E54E9);" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 15, 0.2, 0, 4);"
                )
        );

        return btn;
    }

    public StackPane getView() {
        return rootWrapper;
    }

    // ✔ FIXED — Back button works
    public static void open(Stage stage) {
        MenuScreen menu = new MenuScreen(stage);
        Scene scene = new Scene(menu.getView(), 900, 650);

        stage.setTitle("Game Hub");
        stage.setScene(scene);  // <-- REQUIRED
        stage.show();
    }
}
