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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TSPUI {

    private Stage stage;
    private BorderPane root;

    private TextField playerName;
    private ChoiceBox<Character> homeCityChoice;
    private List<CheckBox> cityChecks = new ArrayList<>();
    private TextArea output;
    private CityMapPane mapPane;

    private int[][] distanceMatrix = new int[10][10];
    private boolean matrixGenerated = false;

    private final char[] cities = {'A','B','C','D','E','F','G','H','I','J'};

    // Multiple choice fields
    private ToggleGroup routeChoices;
    private VBox choicesBox;
    private Button submitRouteBtn;
    private String correctRoute;

    public TSPUI(Stage stage) {
        this.stage = stage;

        root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #74ebd5, #ACB6E5);");

        // ===== Title =====
        Label title = new Label("Traveling Salesman Problem");
        title.setFont(Font.font("Arial", 36));
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        VBox leftBox = new VBox(20);
        leftBox.setAlignment(Pos.TOP_CENTER);

        // Player name
        playerName = new TextField();
        playerName.setPromptText("Enter Player Name");
        playerName.setMaxWidth(250);

        // Home city
        homeCityChoice = new ChoiceBox<>();
        for (char c : cities) homeCityChoice.getItems().add(c);
        homeCityChoice.setValue(cities[new Random().nextInt(cities.length)]);
        HBox homeBox = new HBox(10, new Label("Home City:"), homeCityChoice);
        homeBox.setAlignment(Pos.CENTER);

        // City selection
        FlowPane cityBox = new FlowPane(10, 10);
        cityBox.setAlignment(Pos.CENTER);
        cityBox.setPadding(new Insets(10));
        cityBox.setStyle("-fx-background-color: rgba(255,255,255,0.3); -fx-background-radius: 15;");
        for (char c : cities) {
            CheckBox cb = new CheckBox(String.valueOf(c));
            cityChecks.add(cb);
            cityBox.getChildren().add(cb);
        }

        // Buttons
        Button genBtn = createButton("Generate Random Distances", "#ff9966", "#ff5e62");
        genBtn.setOnAction(e -> generateRandomDistances());

        Button bruteBtn = createButton("Brute Force", "#36d1dc", "#5b86e5");
        bruteBtn.setOnAction(e -> solveTSP("Brute Force"));

        Button nnBtn = createButton("Nearest Neighbor", "#f7971e", "#ffd200");
        nnBtn.setOnAction(e -> solveTSP("Nearest Neighbor"));

        Button gaBtn = createButton("Genetic Algorithm", "#11998e", "#38ef7d");
        gaBtn.setOnAction(e -> solveTSP("Genetic Algorithm"));

        HBox buttons = new HBox(15, bruteBtn, nnBtn, gaBtn);
        buttons.setAlignment(Pos.CENTER);

        // Output area
        output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(120);

        // Multiple choice UI
        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER);
        choicesBox.setPadding(new Insets(10));
        choicesBox.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-background-radius: 15;");
        routeChoices = new ToggleGroup();

        submitRouteBtn = createButton("Submit Route", "#36d1dc", "#5b86e5");
        submitRouteBtn.setOnAction(e -> checkSelectedRoute());
        submitRouteBtn.setDisable(true);

        // Back button
        Button backBtn = createButton("Back to Menu", "#6a11cb", "#2575fc");
        backBtn.setOnAction(e -> com.eightqueens.ui.MenuScreen.open(stage));

        leftBox.getChildren().addAll(
                playerName,
                homeBox,
                cityBox,
                genBtn,
                buttons,
                choicesBox,
                submitRouteBtn,
                output,
                backBtn
        );

        // Map Pane on the right
        mapPane = new CityMapPane();
        mapPane.setDistanceMatrix(distanceMatrix);

        HBox mainBox = new HBox(30, leftBox, mapPane);
        mainBox.setAlignment(Pos.CENTER);

        root.setCenter(mainBox);

        stage.setScene(new Scene(root, 1200, 750));
        stage.setTitle("Traveling Salesman Problem");
        stage.show();
    }

    private Button createButton(String text, String c1, String c2) {
        Button btn = new Button(text);
        btn.setFont(Font.font(15));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(220);
        btn.setStyle("-fx-background-radius: 25; -fx-background-color: linear-gradient(to right,"+c1+","+c2+");");
        btn.setEffect(new DropShadow());
        return btn;
    }

    private void generateRandomDistances() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++)
            for (int j = i + 1; j < 10; j++)
                distanceMatrix[i][j] = distanceMatrix[j][i] = 50 + rand.nextInt(51);

        matrixGenerated = true;
        output.setText("✅ Distance matrix generated (50–100 km).");
        mapPane.setDistanceMatrix(distanceMatrix);
    }

    private void solveTSP(String algorithm) {

        if (!matrixGenerated) {
            output.setText("⚠ Generate distances first!");
            return;
        }

        String player = playerName.getText().trim();
        if (player.isEmpty()) {
            output.setText("⚠ Enter player name!");
            return;
        }

        char home = homeCityChoice.getValue();
        List<Character> selected = new ArrayList<>();
        for (CheckBox cb : cityChecks) {
            if (cb.isSelected()) {
                char city = cb.getText().charAt(0);
                if (city == home) {
                    output.setText("⚠ Home city cannot be selected!");
                    return;
                }
                selected.add(city);
            }
        }

        if (selected.isEmpty()) {
            output.setText("⚠ Select at least one city!");
            return;
        }

        TSPSolution solution;
        try {
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
                default:
                    throw new IllegalArgumentException("Unknown algorithm");
            }
        } catch (Exception ex) {
            output.setText("❌ Error: " + ex.getMessage());
            return;
        }

        correctRoute = solution.getRoute();

        // Generate 4 options (1 correct + 3 random incorrect)
        List<String> options = generateRouteOptions(correctRoute, selected, home);
        Collections.shuffle(options);

        // Display options
        choicesBox.getChildren().clear();
        routeChoices.getToggles().clear();
        for (String opt : options) {
            RadioButton rb = new RadioButton(opt);
            rb.setToggleGroup(routeChoices);
            choicesBox.getChildren().add(rb);
        }
        submitRouteBtn.setDisable(false);

        // Draw correct route on map
        mapPane.drawRoute(parseRoute(correctRoute));
    }

    private List<String> generateRouteOptions(String correct, List<Character> selected, char home) {
        List<String> options = new ArrayList<>();
        options.add(correct);
        Random rand = new Random();
        int tries = 0;
        while (options.size() < 4 && tries < 20) {
            List<Character> shuffled = new ArrayList<>(selected);
            Collections.shuffle(shuffled);
            StringBuilder sb = new StringBuilder();
            sb.append(home).append("→");
            for (char c : shuffled) sb.append(c).append("→");
            sb.append(home);
            String routeStr = sb.toString();
            if (!options.contains(routeStr)) options.add(routeStr);
            tries++;
        }
        return options;
    }

    private void checkSelectedRoute() {
        RadioButton selectedBtn = (RadioButton) routeChoices.getSelectedToggle();
        if (selectedBtn == null) {
            output.setText("⚠ Select a route first!");
            return;
        }

        String chosenRoute = selectedBtn.getText();
        if (chosenRoute.equals(correctRoute)) {
            output.setText("✅ CORRECT! You win!");
        } else {
            output.setText("❌ INCORRECT! You lost!\nCorrect Route: " + correctRoute);
        }

        submitRouteBtn.setDisable(true);
    }

    private List<Character> parseRoute(String route) {
        List<Character> list = new ArrayList<>();
        for (char c : route.toCharArray()) {
            if (c >= 'A' && c <= 'J') list.add(c);
        }
        return list;
    }

    public static void open(Stage stage) {
        new TSPUI(stage);
    }
}
