package com.eightqueens.ui;

import com.gamemenu.GameMenuController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MenuScreen {

    private VBox root;
    private Stage stage;
    private GameMenuController controller;

    public MenuScreen(Stage stage) {
        this.stage = stage;
        this.controller = new GameMenuController(stage);

        // Root container with gradient background
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2193b0, #6dd5ed);");

        // Title
        Button title = new Button("=== Game Hub ===");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        title.setDisable(true);
        title.setStyle("-fx-background-color: transparent;");

        // Create buttons for games
        Button btnEightQueens = createGameButton("Eight Queens Puzzle");
        Button btnTowerOfHanoi = createGameButton("Tower of Hanoi");
        Button btnTSP = createGameButton("Traveling Salesman Problem");
        Button btnSnakeLadder = createGameButton("Snake & Ladder Game");
        Button btnTraffic = createGameButton("Traffic Simulation");

        // Button actions
        btnEightQueens.setOnAction(controller::onEightQueensClick);
        btnTowerOfHanoi.setOnAction(controller::onTowerOfHanoiClick);
        btnTSP.setOnAction(controller::onGame3Click);
        btnSnakeLadder.setOnAction(controller::onGame4Click);
        btnTraffic.setOnAction(controller::onGame5Click);

        // Add all to root
        root.getChildren().addAll(
                title,
                btnEightQueens,
                btnTowerOfHanoi,
                btnTSP,
                btnSnakeLadder,
                btnTraffic
        );
    }

    private Button createGameButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, 18));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b);");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0,0,0,0.25));
        shadow.setRadius(10);
        btn.setEffect(shadow);

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, #ff4b2b, #ff416c);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b);"));

        return btn;
    }

    public VBox getView() {
        return root;
    }

    public static void open(Stage stage) {
        MenuScreen menu = new MenuScreen(stage);
        Scene scene = new Scene(menu.getView(), 800, 600);
        stage.setTitle("Game Hub");
        stage.setScene(scene);
        stage.show();
    }
}
