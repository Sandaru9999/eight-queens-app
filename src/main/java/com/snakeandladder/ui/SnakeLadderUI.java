package com.snakeandladder.ui;

import com.eightqueens.ui.MenuScreen;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class SnakeLadderUI {

    private Stage stage;
    private BorderPane root;
    private GridPane boardGrid;
    private TextArea output;
    private Button rollDiceBtn, backBtn;

    private Label diceDisplay;
    private Label playerNameLabel;   // <<--- NEW

    private String playerName;        // <<--- NEW

    private int N;
    private Map<Integer, Integer> snakes = new HashMap<>();
    private Map<Integer, Integer> ladders = new HashMap<>();
    private Map<Integer, StackPane> cellMap = new HashMap<>();
    private Label playerEmoji;

    private int playerPosition = 1;
    private Random rand = new Random();

    // <<--- constructor updated
    public SnakeLadderUI(Stage stage, String playerName) {
        this.stage = stage;
        this.playerName = playerName;

        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #74ebd5, #9face6);");

        // Output Log
        output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(140);
        output.setStyle("-fx-font-size: 15px; -fx-background-radius: 12px; -fx-text-fill: #333;");

        // Buttons
        rollDiceBtn = new Button("🎲 Roll Dice");
        rollDiceBtn.setFont(Font.font(18));
        rollDiceBtn.setStyle(buttonStyle());
        rollDiceBtn.setOnAction(e -> rollDice());

        backBtn = new Button("⬅ Back");
        backBtn.setFont(Font.font(16));
        backBtn.setStyle(buttonStyle());
        backBtn.setOnAction(e -> MenuScreen.open(stage));

        // Player Name Label
        playerNameLabel = new Label("👤 Player: " + playerName);
        playerNameLabel.setFont(Font.font(24));
        playerNameLabel.setStyle("""
                -fx-background-color: white;
                -fx-padding: 10px 25px;
                -fx-background-radius: 20px;
                -fx-font-weight: bold;
                -fx-text-fill: #333;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);
                """);

        // Dice Display Box
        diceDisplay = new Label("🎲 -");
        diceDisplay.setFont(Font.font(30));
        diceDisplay.setStyle("""
                -fx-background-color: white;
                -fx-padding: 12px 25px;
                -fx-background-radius: 20px;
                -fx-font-weight: bold;
                -fx-text-fill: #333;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);
                """);

        VBox diceBox = new VBox(diceDisplay);
        diceBox.setAlignment(Pos.CENTER);

        // Buttons Row
        HBox buttonRow = new HBox(20, rollDiceBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        // NEW >>> Combine Player Name + Dice + Buttons
        VBox topArea = new VBox(15, playerNameLabel, diceBox, buttonRow);
        topArea.setAlignment(Pos.CENTER);
        topArea.setStyle("-fx-padding: 10px;");

        root.setTop(topArea);
        root.setBottom(output);

        generateBoard();
        drawBoard();

        Scene scene = new Scene(root, 850, 900);
        stage.setScene(scene);
        stage.setTitle("🐍 Snake & Ladder 🪜");
        stage.show();
    }

    private String buttonStyle() {
        return """
               -fx-background-color: #ffffff;
               -fx-background-radius: 15px;
               -fx-padding: 10px 18px;
               -fx-text-fill: #333;
               -fx-font-weight: bold;
               -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);
               """;
    }

    private void generateBoard() {
        N = 8;
        int numSnakes = N - 2;
        int numLadders = N - 2;

        snakes.clear();
        ladders.clear();

        while (snakes.size() < numSnakes) {
            int start = 2 + rand.nextInt(N * N - 2);
            int end = 1 + rand.nextInt(start - 1);
            snakes.put(start, end);
        }

        while (ladders.size() < numLadders) {
            int start = 1 + rand.nextInt(N * N - 2);
            int end = start + 1 + rand.nextInt(N * N - start - 1);
            ladders.put(start, end);
        }

        output.setText("✨ New Board Generated!\nClick 🎲 Roll Dice to start.");
    }

    private void drawBoard() {
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(3);
        boardGrid.setVgap(3);
        boardGrid.setStyle("-fx-padding: 15px;");

        int num = 1;
        boolean leftToRight = true;
        cellMap.clear();

        for (int r = N - 1; r >= 0; r--) {
            for (int c = 0; c < N; c++) {

                int col = leftToRight ? c : (N - 1 - c);

                StackPane cell = new StackPane();
                cell.setPrefSize(90, 90);
                cell.setStyle("""
                        -fx-border-radius: 12px;
                        -fx-background-radius: 12px;
                        -fx-background-color: #ffffffAA;
                        -fx-border-color: #444;
                        -fx-border-width: 1px;
                        """);

                Label lbl = new Label(String.valueOf(num));
                lbl.setFont(Font.font(16));
                lbl.setStyle("-fx-font-weight: bold;");

                cell.getChildren().add(lbl);

                boardGrid.add(cell, col, r);
                cellMap.put(num, cell);
                num++;
            }
            leftToRight = !leftToRight;
        }

        // Player Emoji 😀
        playerEmoji = new Label("😀");
        playerEmoji.setFont(Font.font(32));
        cellMap.get(1).getChildren().add(playerEmoji);

        // Draw Snakes 🐍
        for (var e : snakes.entrySet()) {
            Label snake = new Label("🐍→" + e.getValue());
            snake.setFont(Font.font(18));
            snake.setTextFill(Color.DARKRED);
            StackPane.setAlignment(snake, Pos.TOP_LEFT);
            cellMap.get(e.getKey()).getChildren().add(snake);
        }

        // Draw Ladders 🪜
        for (var e : ladders.entrySet()) {
            Label ladder = new Label("🪜→" + e.getValue());
            ladder.setFont(Font.font(18));
            ladder.setTextFill(Color.DARKGREEN);
            StackPane.setAlignment(ladder, Pos.BOTTOM_RIGHT);
            cellMap.get(e.getKey()).getChildren().add(ladder);
        }

        root.setCenter(boardGrid);
    }

    private void rollDice() {
        int dice = 1 + rand.nextInt(6);

        // Update the big dice display 🎲
        diceDisplay.setText("🎲 " + dice);

        output.appendText("\nDice rolled: " + dice);

        int nextPos = playerPosition + dice;
        if (nextPos > N * N) nextPos = playerPosition;

        if (snakes.containsKey(nextPos)) {
            nextPos = snakes.get(nextPos);
            output.appendText(" → 🐍 Snake! Go to " + nextPos);
        } else if (ladders.containsKey(nextPos)) {
            nextPos = ladders.get(nextPos);
            output.appendText(" → 🪜 Ladder! Go to " + nextPos);
        }

        animateMove(playerPosition, nextPos);
        playerPosition = nextPos;

        if (playerPosition == N * N) {
            output.appendText("\n🎉 YOU WIN!");
        }
    }

    private void animateMove(int from, int to) {
        StackPane oldCell = cellMap.get(from);
        StackPane newCell = cellMap.get(to);

        oldCell.getChildren().remove(playerEmoji);
        newCell.getChildren().add(playerEmoji);

        TranslateTransition tt = new TranslateTransition(Duration.millis(200));
        tt.setNode(playerEmoji);
        tt.setFromY(-5);
        tt.setToY(5);
        tt.play();
    }

    // overload open() to keep old usage
    public static void open(Stage stage) {
        new SnakeLadderUI(stage, "Player 1");
    }

    // new version to pass actual name
    public static void open(Stage stage, String playerName) {
        new SnakeLadderUI(stage, playerName);
    }
}
