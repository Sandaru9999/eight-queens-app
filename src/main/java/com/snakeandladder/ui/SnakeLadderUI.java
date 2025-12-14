package com.snakeandladder.ui;

import com.snakeandladder.logic.BoardGenerator; 
import com.snakeandladder.db.SnakeLadderDAO;
import com.snakeandladder.logic.SnakeLadderBFS;
import com.snakeandladder.logic.SnakeLadderDP;
import com.snakeandladder.models.SnakeLadderSolution;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage; 

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SnakeLadderUI {

    private Stage stage;
    private BorderPane root;
    private GridPane boardGrid;
    private TextArea outputArea;
    private int N = 9; 
    private SnakeLadderSolution gameData;
    
    private Map<Integer, Integer> currentSnakes;
    private Map<Integer, Integer> currentLadders;
    private Map<Integer, Integer> reverseSnakes = new HashMap<>(); 
    private Map<Integer, Integer> reverseLadders = new HashMap<>(); 

    private static final String FONT_FAMILY = "Montserrat"; 
    private static final double SCENE_WIDTH = 1250; 
    private static final double SCENE_HEIGHT = 850;
    private static final double BOARD_DISPLAY_SIZE = 650.0; 

    
    private static final String COLOR_PRIMARY = "#2c3e50"; 
    private static final String COLOR_VIBRANT_BLUE = "#3498db";
    private static final String COLOR_LADDER_BRIGHT = "#ffc300"; 
    private static final String COLOR_SNAKE_DULL = "#27ae60"; 
    private static final String COLOR_ACCENT = "#1abc9c"; 
    private static final String COLOR_DANGER = "#e74c3c"; 
    private static final String COLOR_BACKGROUND = "#ecf0f1"; 
    
    
    private static final String COLOR_CELL_A = "#34495e"; 
    private static final String COLOR_CELL_B = "#4a627a"; 
    
    private static final String STYLE_CARD = 
        "-fx-background-color: #FFFFFF; " +
        "-fx-border-radius: 20; " +
        "-fx-background-radius: 20; " +
        "-fx-padding: 30; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);";
        
    private static final String STYLE_GAME_BUTTON_PRIMARY = 
        "-fx-background-color: " + COLOR_VIBRANT_BLUE + "; " + 
        "-fx-text-fill: white; " + 
        "-fx-font-weight: bold; " + 
        "-fx-font-size: 18px; " + 
        "-fx-padding: 18 45; " + 
        "-fx-background-radius: 12;";
    
    private static final String STYLE_TITLE_1ST_PAGE = 
        "-fx-font-size: 40px; " + 
        "-fx-font-weight: 800; " + 
        "-fx-text-fill: " + COLOR_PRIMARY + ";"; 
        
    private static final String STYLE_SUBTITLE_1ST_PAGE = 
        "-fx-font-size: 18px; -fx-text-fill: #555; -fx-padding: 0 0 20 0;";
        
    private static final String STYLE_BUTTON_1ST_PAGE = 
        "-fx-background-color: #007bff; " +
        "-fx-text-fill: white; " + 
        "-fx-font-weight: bold; " + 
        "-fx-font-size: 16px; " + 
        "-fx-padding: 12 30; " + 
        "-fx-background-radius: 10;";
        
    private static final String STYLE_INPUT_1ST_PAGE = 
        "-fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #ddd; -fx-border-radius: 8;";


    public SnakeLadderUI(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + COLOR_BACKGROUND + "; -fx-font-family: " + FONT_FAMILY + ";");

        
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(100);
        outputArea.setFont(Font.font("Monospaced", 10));
        outputArea.setStyle("-fx-control-inner-background:#1f2937; -fx-text-fill: #99f6e4; -fx-background-radius: 0; -fx-border-width: 0;");
        
        root.setBottom(outputArea);

        showInputScreen();

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT); 
        stage.setScene(scene);
        stage.setTitle("Snake and Ladder Solver"); 
        stage.show();
    }
    
    
    private void showInputScreen() {
        root.setCenter(null);
        root.setRight(null);
        
        
        Label mainTitle = new Label("🐍 Snake and Ladder 🪜");
        mainTitle.setStyle(STYLE_TITLE_1ST_PAGE);
        
        Label subTitle = new Label("Determine the minimum throws required to win on a random board.");
        subTitle.setStyle(STYLE_SUBTITLE_1ST_PAGE);

        
        VBox inputCard = new VBox(25);
        inputCard.setStyle(STYLE_CARD);
        inputCard.setAlignment(Pos.TOP_LEFT); 
        inputCard.setMaxWidth(450); 
        
        
        Label nameLabel = createHeaderLabel("Player Name", "#333");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name (e.g., Alice)");
        nameInput.setText("Sachala"); 
        nameInput.setStyle(STYLE_INPUT_1ST_PAGE);
        
        VBox nameControl = new VBox(5, nameLabel, nameInput);

        
        Label sizeTitle = createHeaderLabel("Board Size (N x N)", "#333");
        sizeTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        TextField sizeInput = new TextField(String.valueOf(N));
        sizeInput.setPromptText("Enter N (6 to 12)");
        sizeInput.setMaxWidth(150);
        sizeInput.setStyle(STYLE_INPUT_1ST_PAGE);
        
        Label cellsLabel = new Label("Total Cells: " + (N*N));
        cellsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777;");
        
        sizeInput.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int newN = Integer.parseInt(newVal);
                cellsLabel.setText("Total Cells: " + (newN * newN));
            } catch (NumberFormatException e) {
                cellsLabel.setText("Total Cells: Invalid Input");
            }
        });
        
        HBox sizeBox = new HBox(15, sizeInput, cellsLabel);
        sizeBox.setAlignment(Pos.CENTER_LEFT);
        
        VBox sizeControl = new VBox(5, sizeTitle, sizeBox);
        
        
        Button startBtn = new Button("Generate & Start Challenge");
        startBtn.setStyle(STYLE_BUTTON_1ST_PAGE); 
        startBtn.setPrefHeight(50);
        startBtn.setMaxWidth(Double.MAX_VALUE);
        
        startBtn.setOnAction(e -> {
            if (nameInput.getText().trim().isEmpty()) {
                showAlert("Input Error", "Please enter your name.");
                return;
            }
            
            try {
                int inputN = Integer.parseInt(sizeInput.getText().trim());
                if (inputN < 6 || inputN > 12) {
                    showAlert("Input Error", "Board size N must be between 6 and 12.");
                    return;
                }
                N = inputN;
                gameData = new SnakeLadderSolution(); 
                gameData.setPlayerName(nameInput.getText().trim());
                startGameRound();
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter a valid number for board size N.");
            }
        });
        
        inputCard.getChildren().addAll(
            nameControl, 
            sizeControl, 
            startBtn
        );

        VBox contentLayout = new VBox(20, mainTitle, subTitle, inputCard);
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.setPadding(new Insets(50));
        
        ScrollPane scrollPane = new ScrollPane(contentLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);
    }
    
    
    private void startGameRound() {
        
        BoardGenerator generator = new BoardGenerator(N);
        currentSnakes = generator.generateSnakes();
        currentLadders = generator.generateLadders();
        
        reverseSnakes = currentSnakes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        reverseLadders = currentLadders.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        
        SnakeLadderBFS bfsSolver = new SnakeLadderBFS(N, currentSnakes, currentLadders);
        SnakeLadderDP dpSolver = new SnakeLadderDP(N, currentSnakes, currentLadders);
        
        Map<String, Long> bfsResult = bfsSolver.solveAndMeasureTime();
        Map<String, Long> dpResult = dpSolver.solveAndMeasureTime();
        
        int bfsThrows = bfsResult.get("minThrows").intValue();
        int dpThrows = dpResult.get("minThrows").intValue();
        
        int correctThrows = (bfsThrows >= 0 && dpThrows >= 0) ? Math.min(bfsThrows, dpThrows) : Math.max(bfsThrows, dpThrows);
        if (correctThrows < 0) correctThrows = Math.max(bfsThrows, dpThrows);

        List<Integer> optimalPath = bfsSolver.getOptimalPath(); 
        
        gameData.setBoardSize(N);
        gameData.setSnakes(currentSnakes);
        gameData.setLadders(currentLadders);
        gameData.setCorrectMinThrows(correctThrows);
        gameData.setAlgo1TimeNanos(bfsResult.get("timeNanos"));
        gameData.setAlgo2TimeNanos(dpResult.get("timeNanos"));
        gameData.setOptimalPath(optimalPath); 

        outputArea.appendText("Algorithms Solved: BFS Time: " + gameData.getAlgo1TimeNanos() / 1_000_000.0 + "ms, DP Time: " + gameData.getAlgo2TimeNanos() / 1_000_000.0 + "ms\n");
        outputArea.appendText("Correct minimum throws: " + correctThrows + ". Player: " + gameData.getPlayerName() + "\n");
        
        
        drawBoard(null); 
        
        
        startGameScreen(correctThrows);
    }

    
    
    private void startGameScreen(int correctThrows) {
        
        
        VBox challengeCard = new VBox(20);
        challengeCard.setStyle(STYLE_CARD);
        challengeCard.setMaxWidth(300);
        
        Label challengeTitle = new Label("Your Challenge");
        challengeTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + COLOR_PRIMARY + ";");
        
        Label question = new Label("What is the minimum number of throws required to reach cell " + N*N + "?");
        question.setWrapText(true);
        question.setStyle("-fx-font-size: 14px;");
        
        List<Integer> choices = generateChoices(correctThrows, 3);
        ToggleGroup group = new ToggleGroup();
        VBox choiceBox = new VBox(15);
        choiceBox.setPadding(new Insets(10, 0, 10, 0));
        
        for (int choice : choices) {
            ToggleButton choiceBtn = new ToggleButton(String.valueOf(choice) + " Moves"); 
            choiceBtn.setPrefWidth(250);
            choiceBtn.setToggleGroup(group); 
            choiceBtn.setUserData(choice);
            choiceBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #333; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            
            choiceBtn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                if (isSelected) {
                    choiceBtn.setStyle("-fx-background-color: #dbeaff; -fx-border-color: " + COLOR_VIBRANT_BLUE + "; -fx-border-width: 2px; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20; -fx-text-fill: " + COLOR_PRIMARY + ";");
                } else {
                    choiceBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #333; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
                }
            });
            
            choiceBox.getChildren().add(choiceBtn);
        }

        Button submitBtn = new Button("Submit Guess");
        submitBtn.setStyle(STYLE_GAME_BUTTON_PRIMARY); 
        submitBtn.setPrefWidth(250);

        submitBtn.setOnAction(e -> {
            ToggleButton selected = (ToggleButton) group.getSelectedToggle();
            
            if (selected == null) {
                showAlert("Submission Error", "Please select one of the choices.");
                return;
            }
            
            int playerGuess = (int) selected.getUserData();
            gameData.setPlayerAnswer(playerGuess);
            
            showResultScreen(correctThrows); 
        });

        
        challengeCard.getChildren().addAll(challengeTitle, question, choiceBox, submitBtn);
        challengeCard.setAlignment(Pos.TOP_CENTER);

        
        VBox historyTable = createHistoryAnalyticsTable();
        historyTable.setMaxWidth(550); 
        
        VBox leftContent = new VBox(30, boardGrid, historyTable);
        leftContent.setAlignment(Pos.CENTER);
        leftContent.setPadding(new Insets(20));
        
        ScrollPane scrollPane = new ScrollPane(leftContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);
        
        root.setRight(new VBox(20, challengeCard)); 
        BorderPane.setMargin(root.getRight(), new Insets(20));
    }
    
    
    private void showResultScreen(int correctThrows) {
        boolean isCorrect = gameData.getPlayerAnswer() == correctThrows;

        
        if (gameData.getPlayerAnswer() >= 0) {
            try {
                SnakeLadderDAO dao = new SnakeLadderDAO();
                dao.saveSolution(gameData);
            } catch (SQLException | RuntimeException e) {
                outputArea.appendText("Database/File Persistence Error: " + e.getMessage() + "\n");
            }
        }
        
        
        VBox resultCard = new VBox(30);
        resultCard.setPadding(new Insets(40));
        resultCard.setAlignment(Pos.TOP_CENTER);
        resultCard.setMaxWidth(800);
        resultCard.setStyle(STYLE_CARD);
        
        VBox resultBanner = new VBox(5);
        resultBanner.setAlignment(Pos.CENTER);
        
        String bannerStyle;
        String resultText;
        
        if (isCorrect) {
            bannerStyle = "-fx-background-color: " + COLOR_ACCENT + "; -fx-background-radius: 5;"; 
            resultText = "✅ Correct! Data Saved.";
            outputArea.appendText("\n✅ CORRECT. Data saved.\n");
        } else {
            bannerStyle = "-fx-background-color: " + COLOR_DANGER + "; -fx-background-radius: 5;"; 
            resultText = "❌ Incorrect";
            outputArea.appendText("\n❌ INCORRECT. Correct answer was " + correctThrows + ".\n");
        }
        
        Label resultLabel = new Label(resultText);
        resultLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label moveLabel = new Label("The minimum moves required is " + correctThrows + ".");
        moveLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        
        resultBanner.getChildren().addAll(resultLabel, moveLabel);
        resultBanner.setStyle(bannerStyle + "-fx-padding: 15;");
        
        VBox pathCard = new VBox(15);
        pathCard.setAlignment(Pos.CENTER);
        
        Label pathTitle = new Label("Optimal Path Solution (Total Moves: " + (gameData.getOptimalPath().size() > 0 ? gameData.getOptimalPath().size() - 1 : 0) + ")");
        pathTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        
        HBox pathHBox = createOptimalPathVisualization(gameData.getOptimalPath()); 
        pathCard.getChildren().addAll(pathTitle, pathHBox);
        
        HBox algoCard = new HBox(40);
        algoCard.setAlignment(Pos.CENTER);
        VBox bfsTime = createAlgoTimeBox("ALGORITHM 1: BFS", gameData.getAlgo1TimeNanos() / 1_000_000.0);
        VBox dpTime = createAlgoTimeBox("ALGORITHM 2: DP", gameData.getAlgo2TimeNanos() / 1_000_000.0);
        algoCard.getChildren().addAll(bfsTime, dpTime);

        Button playAgainBtn = new Button("Play Again");
        playAgainBtn.setStyle(STYLE_GAME_BUTTON_PRIMARY);
        playAgainBtn.setPrefWidth(200);
        playAgainBtn.setOnAction(e -> showInputScreen());
        
        resultCard.getChildren().addAll(resultBanner, algoCard, pathCard, playAgainBtn);
        
        
        VBox historyTable = createHistoryAnalyticsTable();
        historyTable.setMaxWidth(700); 

        drawBoard(gameData.getOptimalPath()); 
        
        VBox boardContainer = new VBox(boardGrid);
        boardContainer.setAlignment(Pos.CENTER);

        VBox finalContent = new VBox(30, resultCard, boardContainer, historyTable);
        finalContent.setAlignment(Pos.CENTER);
        finalContent.setPadding(new Insets(20));
        
        ScrollPane scrollPane = new ScrollPane(finalContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);
        root.setRight(null); 
    }

    
    private void drawBoard(List<Integer> highlightPath) {
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(1); 
        boardGrid.setVgap(1); 
        boardGrid.setStyle("-fx-background-color: #1a242f; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 0);");
        
        int num = 1;
        boolean leftToRight = true;
        
        double cellSize = BOARD_DISPLAY_SIZE / N; 

        for (int r = N - 1; r >= 0; r--) {
            for (int c = 0; c < N; c++) {
                int col = leftToRight ? c : (N - 1 - c);
                
                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize); 
                cell.setPadding(new Insets(2)); 
                
                String cellStyle = "-fx-border-width: 0; -fx-background-radius: 0;"; 
                
                boolean isLight = (r % 2 == 0) ? (c % 2 == 0) : (c % 2 != 0);
                String baseColor = isLight ? COLOR_CELL_B : COLOR_CELL_A; 
                
                String cellSpecificStyle = "-fx-background-color: " + baseColor + ";";
                
                Label lblNum = new Label(String.valueOf(num));
                lblNum.setFont(Font.font(FONT_FAMILY, 24 / (N / 4.0))); 
                lblNum.setStyle("-fx-font-weight: 900; -fx-text-fill: #ecf0f1;"); 
                
                StackPane.setAlignment(lblNum, Pos.BOTTOM_RIGHT); 
                StackPane.setMargin(lblNum, new Insets(5));
                cell.getChildren().add(lblNum);
                
                if (currentSnakes.containsKey(num) || currentLadders.containsKey(num)) {
                    VBox iconContainer = new VBox(5);
                    iconContainer.setAlignment(Pos.TOP_LEFT);
                    StackPane.setMargin(iconContainer, new Insets(5));

                    Label transferLbl = new Label();
                    int destination = 0;
                    String color = "";
                    String tooltipText = "";
                    
                    if (currentSnakes.containsKey(num)) {
                        destination = currentSnakes.get(num);
                        color = COLOR_SNAKE_DULL;
                        transferLbl.setText("🐍↓ TO " + destination);
                        tooltipText = "Snake Head: " + num + " down to " + destination;
                    } else if (currentLadders.containsKey(num)) {
                        destination = currentLadders.get(num);
                        color = COLOR_LADDER_BRIGHT;
                        transferLbl.setText("🪜↑ TO " + destination);
                        tooltipText = "Ladder Base: " + num + " up to " + destination;
                    }
                    
                    transferLbl.setFont(Font.font(FONT_FAMILY, 10 + (9 - N) / 2.0)); 
                    transferLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a242f; -fx-background-color: " + color + "; -fx-padding: 2 5; -fx-background-radius: 4;");
                    iconContainer.getChildren().add(transferLbl);
                    cell.getChildren().add(iconContainer);
                    Tooltip.install(cell, new Tooltip(tooltipText));
                }

                if (highlightPath != null && highlightPath.contains(num)) {
                    cellSpecificStyle += "-fx-border-width: 4px; -fx-border-color: #f1c40f; -fx-effect: dropshadow(gaussian, rgba(241,196,15,0.8), 5, 0, 0, 0);"; 
                }
                
                cell.setStyle(cellStyle + cellSpecificStyle);

                if (num == 1) {
                    Label startLbl = new Label("START");
                    startLbl.setFont(Font.font(FONT_FAMILY, 14));
                    startLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #1a242f; -fx-background-color: " + COLOR_ACCENT + "; -fx-padding: 5 10; -fx-background-radius: 5;");
                    StackPane.setAlignment(startLbl, Pos.CENTER);
                    cell.getChildren().add(startLbl);
                    cell.setStyle(cellStyle + "-fx-background-color: #89e7d9;"); 
                }
                
                if (num == N*N) {
                    Label goalLbl = new Label("GOAL");
                    goalLbl.setFont(Font.font(FONT_FAMILY, 16));
                    goalLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: white; -fx-background-color: " + COLOR_DANGER + "; -fx-padding: 5 10; -fx-background-radius: 5;");
                    StackPane.setAlignment(goalLbl, Pos.CENTER);
                    cell.getChildren().add(goalLbl);
                    cell.setStyle(cellStyle + "-fx-background-color: #f7a29a;"); 
                }
                
                boardGrid.add(cell, col, r);
                num++;
            }
            leftToRight = !leftToRight;
        }

        VBox boardContainer = new VBox(boardGrid);
        boardContainer.setAlignment(Pos.CENTER);
        
        root.setCenter(boardContainer); 
        BorderPane.setMargin(boardContainer, new Insets(10));
    }
    
    
    
    private VBox createAlgoTimeBox(String title, double timeMs) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: " + COLOR_VIBRANT_BLUE + ";"); 
        Label timeLbl = new Label(String.format("%.4f ms", timeMs));
        timeLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
        Label executionLbl = new Label("Execution Time");
        executionLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
        box.getChildren().addAll(titleLbl, timeLbl, executionLbl);
        box.setPrefWidth(200); 
        box.setStyle("-fx-border-color: #eee; -fx-border-radius: 8; -fx-padding: 15; -fx-background-color: #f9f9f9;"); 
        return box;
    }

    private VBox createHistoryAnalyticsTable() {
        VBox container = new VBox(10);
        container.setStyle(STYLE_CARD);
        container.setPadding(new Insets(15));
        container.setMaxWidth(Double.MAX_VALUE);

        Label title = new Label("Game History & Analytics");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
        container.getChildren().add(title);
        
        GridPane table = new GridPane();
        table.setHgap(15);
        table.setVgap(8);
        table.setPadding(new Insets(10, 0, 0, 0));

        
        ColumnConstraints col1 = new ColumnConstraints(); col1.setMinWidth(60); 
        ColumnConstraints col2 = new ColumnConstraints(); col2.setMinWidth(80);
        ColumnConstraints col3 = new ColumnConstraints(); col3.setMinWidth(80);
        ColumnConstraints col4 = new ColumnConstraints(); col4.setMinWidth(80);
        ColumnConstraints col5 = new ColumnConstraints(); col5.setMinWidth(100);
        ColumnConstraints col6 = new ColumnConstraints(); col6.setMinWidth(100); 
        table.getColumnConstraints().addAll(col1, col2, col3, col4, col5, col6);

        String headerStyle = "-fx-font-weight: bold; -fx-text-fill: #555; -fx-font-size: 12px; -fx-border-width: 0 0 1 0; -fx-border-color: #ccc; -fx-padding: 0 0 5 0;";
        
        
        Label timeHeader = new Label("#"); timeHeader.setStyle(headerStyle); table.add(timeHeader, 0, 0); 
        Label playerHeader = new Label("PLAYER"); playerHeader.setStyle(headerStyle); table.add(playerHeader, 1, 0);
        Label boardHeader = new Label("BOARD(N)"); boardHeader.setStyle(headerStyle); table.add(boardHeader, 2, 0);
        Label resultHeader = new Label("RESULT"); resultHeader.setStyle(headerStyle); table.add(resultHeader, 3, 0);
        Label bfsHeader = new Label("BFS TIME(ms)"); bfsHeader.setStyle(headerStyle); table.add(bfsHeader, 4, 0);
        
        Label dpHeader = new Label("DP TIME(ms)"); dpHeader.setStyle(headerStyle); table.add(dpHeader, 5, 0); 

        try {
            SnakeLadderDAO dao = new SnakeLadderDAO();
            
            List<SnakeLadderSolution> history = dao.getAllSolutions(5); 
            
            for (int i = 0; i < history.size(); i++) {
                SnakeLadderSolution entry = history.get(i);
                int row = i + 1;
                
                String resultText = (entry.getPlayerAnswer() == entry.getCorrectMinThrows()) ? "WIN" : "LOSS";
                String resultColor = (resultText.equals("WIN")) ? COLOR_ACCENT : COLOR_DANGER;
                
                
                String entryNumStr = String.valueOf(history.size() - i); 

                String cellStyle = "-fx-font-size: 12px; -fx-padding: 2 0;";

                table.add(new Label(entryNumStr), 0, row);
                table.add(new Label(entry.getPlayerName()), 1, row);
                table.add(new Label(entry.getBoardSize() + "x" + entry.getBoardSize()), 2, row);
                
                Label resultLbl = new Label(resultText);
                resultLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: " + resultColor + ";" + cellStyle);
                table.add(resultLbl, 3, row);
                
                
                Label bfsTimeLbl = new Label(String.format("%.2f", entry.getAlgo1TimeNanos() / 1_000_000.0));
                bfsTimeLbl.setStyle(cellStyle);
                table.add(bfsTimeLbl, 4, row);
                
                
                Label dpTimeLbl = new Label(String.format("%.2f", entry.getAlgo2TimeNanos() / 1_000_000.0));
                dpTimeLbl.setStyle(cellStyle);
                table.add(dpTimeLbl, 5, row);
            }

        } catch (SQLException | RuntimeException e) {
            Label errorLabel = new Label("Could not load history (DAO): " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: " + COLOR_DANGER + ";");
            container.getChildren().add(errorLabel);
            outputArea.appendText("Error loading history: " + e.getMessage() + "\n");
        }
        
        container.getChildren().add(table);
        return container;
    }

    private HBox createOptimalPathVisualization(List<Integer> path) {
        HBox pathBox = new HBox(5);
        pathBox.setAlignment(Pos.CENTER);
        pathBox.setPadding(new Insets(5));
        pathBox.setSpacing(10); 

        if (path == null || path.isEmpty()) {
              Label errorLabel = new Label("Path not available (Board may be blocked).");
              pathBox.getChildren().add(errorLabel);
              return new HBox(pathBox); 
        }

        int displayLimit = 10; 
        
        for (int i = 0; i < path.size(); i++) {
            boolean isGoal = i == path.size() - 1;
            boolean isStart = i == 0;
            
            if (i >= displayLimit && !isGoal) {
                if (i == path.size() - 2) {
                    
                } else if (i == displayLimit) {
                    Label dots = new Label("...");
                    dots.setStyle("-fx-font-size: 20px; -fx-text-fill: #999;");
                    pathBox.getChildren().add(dots);
                }
                if (i < path.size() - 1) {
                    continue; 
                }
            }
            
            int cellNum = path.get(i);
            String arrow = "";
            String cellStyle = "-fx-background-radius: 20; -fx-padding: 8 12; -fx-text-fill: white; -fx-font-weight: bold;";
            String cellColor;
            
            if (isStart) {
                cellColor = COLOR_ACCENT; 
                arrow = "";
            } else if (isGoal) {
                cellColor = COLOR_DANGER; 
                arrow = "➡️";
            } else {
                cellColor = COLOR_VIBRANT_BLUE; 
                arrow = "➡️";
            }
            
            if (!isStart) {
                Label arrowLabel = new Label(arrow);
                arrowLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
                pathBox.getChildren().add(arrowLabel);
            }

            Label cellLabel = new Label(String.valueOf(cellNum));
            cellLabel.setStyle(cellStyle + "-fx-background-color: " + cellColor + ";");
            pathBox.getChildren().add(cellLabel);
        }
        
        ScrollPane pathScrollPane = new ScrollPane(pathBox);
        pathScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        pathScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pathScrollPane.setFitToHeight(true);
        pathScrollPane.setMaxHeight(60);
        pathScrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #eee; -fx-border-radius: 8;");
        
        HBox container = new HBox(pathScrollPane);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(Double.MAX_VALUE);
        return container;
    }
    
    private Label createHeaderLabel(String text, String color) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + color + ";");
        return label;
    }

    private List<Integer> generateChoices(int correctThrows, int limit) {
        Set<Integer> choices = new HashSet<>();
        choices.add(correctThrows);
        
        if (correctThrows > 2) choices.add(correctThrows - 1);
        choices.add(correctThrows + 1);
        choices.add(correctThrows + 2);
        
        while (choices.size() < limit + 1) { 
            choices.add(Collections.max(choices) + 1);
        }
        
        List<Integer> sortedChoices = new ArrayList<>(choices);
        Collections.sort(sortedChoices);
        
        return sortedChoices.stream().filter(c -> c > 0).limit(limit).collect(Collectors.toList());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}