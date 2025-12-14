package com.tsp.ui;

import com.tsp.db.TSPDAO;
import com.tsp.logic.TSPBruteForce;
import com.tsp.logic.TSPNearestNeighbor;
import com.tsp.logic.TSPGeneticAlgorithm;
import com.tsp.models.TSPSolution;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSPUI {

    private Stage stage;
    private BorderPane root;
    private TextField playerName;
    private ChoiceBox<Character> homeCityChoice;
    private List<CheckBox> cityChecks = new ArrayList<>();
    private TextArea output;
    private int[][] distanceMatrix = new int[10][10];
    private char[] cities = {'A','B','C','D','E','F','G','H','I','J'};

    public TSPUI(Stage stage) {
        this.stage = stage;

        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #74ebd5, #ACB6E5);");

        // ===== Top: Title =====
        Label title = new Label("Traveling Salesman Problem");
        title.setFont(Font.font("Arial", 36));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // ===== Center: Form and city selection =====
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.TOP_CENTER);

        // Player Name
        playerName = new TextField();
        playerName.setPromptText("Enter Player Name");
        playerName.setMaxWidth(250);
        playerName.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-padding: 10;");

        // Home City ChoiceBox
        homeCityChoice = new ChoiceBox<>();
        for (char c : cities) homeCityChoice.getItems().add(c);
        homeCityChoice.setValue(cities[new Random().nextInt(cities.length)]);
        homeCityChoice.setStyle("-fx-background-radius: 10; -fx-padding: 5;");

        HBox homeCityBox = new HBox(10, new Label("Home City:"), homeCityChoice);
        homeCityBox.setAlignment(Pos.CENTER);

        // City selection
        FlowPane cityBox = new FlowPane(10, 10);
        cityBox.setAlignment(Pos.CENTER);
        cityBox.setPadding(new Insets(10));
        cityBox.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 15;");
        for (char c : cities) {
            CheckBox cb = new CheckBox(String.valueOf(c));
            cb.setStyle("-fx-font-weight: bold;");
            cityChecks.add(cb);
            cityBox.getChildren().add(cb);
        }

        // Buttons
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);

        Button randomDistBtn = createModernButton("Generate Random Distances", "#ff9966", "#ff5e62");
        randomDistBtn.setOnAction(e -> generateRandomDistances());

        Button solveBruteBtn = createModernButton("Brute Force", "#36d1dc", "#5b86e5");
        solveBruteBtn.setOnAction(e -> solveTSP("Brute Force"));

        Button solveNNBtn = createModernButton("Nearest Neighbor", "#f7971e", "#ffd200");
        solveNNBtn.setOnAction(e -> solveTSP("Nearest Neighbor"));

        Button solveGAButton = createModernButton("Genetic Algorithm", "#11998e", "#38ef7d");
        solveGAButton.setOnAction(e -> solveTSP("Genetic Algorithm"));

        buttons.getChildren().addAll(randomDistBtn, solveBruteBtn, solveNNBtn, solveGAButton);

        // Output area
        output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(250);
        output.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 10; -fx-font-family: Consolas; -fx-font-size: 14;");

        // Back button
        Button backBtn = createModernButton("Back to Menu", "#6a11cb", "#2575fc");
        backBtn.setOnAction(e -> com.eightqueens.ui.MenuScreen.open(stage));

        centerBox.getChildren().addAll(playerName, homeCityBox, cityBox, buttons, output, backBtn);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Traveling Salesman Problem");
        stage.show();
    }

    private Button createModernButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Verdana", 16));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(220);
        btn.setPrefHeight(50);
        btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, "+color1+", "+color2+");");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0,0,0,0.25));
        shadow.setRadius(10);
        btn.setEffect(shadow);

        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, "+color2+", "+color1+");"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right, "+color1+", "+color2+");"));
        return btn;
    }

    private void generateRandomDistances() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++)
            for (int j = i + 1; j < 10; j++)
                distanceMatrix[i][j] = distanceMatrix[j][i] = 50 + rand.nextInt(51);

        output.setText("✅ Distance matrix generated randomly (50-100 km).");
    }

    private void solveTSP(String algorithm) {
        String player = playerName.getText().trim();
        if (player.isEmpty()) { output.setText("⚠ Enter player name!"); return; }

        char home = homeCityChoice.getValue();
        List<Character> selected = new ArrayList<>();
        for (CheckBox cb : cityChecks) if (cb.isSelected()) selected.add(cb.getText().charAt(0));
        if (selected.isEmpty()) { output.setText("⚠ Select at least one city!"); return; }

        TSPSolution solution = null;
        long start = System.currentTimeMillis();

        switch (algorithm) {
            case "Brute Force":
                solution = new TSPBruteForce(distanceMatrix, home, selected).solve();
                break;
            case "Nearest Neighbor":
                solution = new TSPNearestNeighbor(distanceMatrix, home, selected).solve();
                break;
            case "Genetic Algorithm":
                solution = new TSPGeneticAlgorithm(distanceMatrix, home, selected).solve();
                break;
        }

        long duration = System.currentTimeMillis() - start;

        if (solution != null) {
            solution.setPlayerName(player);
            solution.setTimeTakenMs(duration);
            solution.setAlgorithm(algorithm);

            output.setText(
                    "✅ Algorithm: " + algorithm +
                    "\n🛣 Shortest Route: " + solution.getRoute() +
                    "\n📏 Total Distance: " + solution.getTotalDistance() + " km" +
                    "\n⏱ Time Taken: " + duration + " ms"
            );

            TSPDAO.savePlayerSolution(solution);
        }
    }

    public static void open(Stage stage) {
        new TSPUI(stage);
    }
}
