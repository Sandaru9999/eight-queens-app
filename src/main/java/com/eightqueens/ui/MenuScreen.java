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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MenuScreen {

    private StackPane rootWrapper;
    private VBox panel;
    private Stage stage;
    private GameMenuController controller;

    public MenuScreen(Stage stage) {
        this.stage = stage;
        this.controller = new GameMenuController(stage);

        // Root: black background with subtle gradient
        rootWrapper = new StackPane();
        rootWrapper.setStyle(
                "-fx-background-color: linear-gradient(to bottom right, #000000, #111111);"
        );
        rootWrapper.setPrefSize(Screen.getPrimary().getBounds().getWidth(),
                                Screen.getPrimary().getBounds().getHeight());

        // Panel: glassy black with shadow
        panel = new VBox(30);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(50));
        panel.setMaxWidth(400);
        panel.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.6),
                new CornerRadii(20),
                Insets.EMPTY
        )));
        BoxBlur blur = new BoxBlur(4, 4, 2);
        panel.setEffect(blur);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(25);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 255, 128, 0.3));
        panel.setEffect(shadow);

        // Title
        Button title = new Button("🎮 GAME HUB");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        title.setTextFill(Color.LIGHTGREEN);
        title.setDisable(true);
        title.setStyle("-fx-background-color: transparent; -fx-effect: dropshadow(gaussian, rgba(0,255,128,0.5), 10, 0.3, 0, 2);");

        // Game buttons
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
        StackPane.setAlignment(panel, Pos.CENTER);
    }

    private Button createGameButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 22));
        btn.setPrefWidth(350);
        btn.setPrefHeight(60);
        btn.setTextFill(Color.BLACK);

        btn.setStyle(
                "-fx-background-radius: 30;" +
                "-fx-background-color: linear-gradient(to right, #00FF7F, #00FA9A);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,255,128,0.4), 12, 0.2, 0, 4);"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-radius: 30;" +
                "-fx-background-color: linear-gradient(to right, #00FA9A, #00FF7F);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,255,128,0.5), 14, 0.25, 0, 6);"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-radius: 30;" +
                "-fx-background-color: linear-gradient(to right, #00FF7F, #00FA9A);" +
                "-fx-effect: dropshadow(gaussian, rgba(0,255,128,0.4), 12, 0.2, 0, 4);"
        ));

        return btn;
    }

    public StackPane getView() {
        return rootWrapper;
    }

    public static void open(Stage stage) {
        MenuScreen menu = new MenuScreen(stage);

        // Full-screen scene
        Scene scene = new Scene(menu.getView(),
                Screen.getPrimary().getBounds().getWidth(),
                Screen.getPrimary().getBounds().getHeight());

        stage.setTitle("Game Hub");
        stage.setScene(scene);
        stage.setFullScreen(true);       // True full-screen
        stage.setFullScreenExitHint(""); // Remove exit hint
        stage.show();
    }
}
