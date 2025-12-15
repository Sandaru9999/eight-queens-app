// package com.snakeandladder.ui;

// import com.eightqueens.ui.MenuScreen;
// import javafx.animation.TranslateTransition;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;
// import javafx.util.Duration;

// import java.util.*;

// public class SnakeLadderUI {

//     private Stage stage;
//     private BorderPane root;
//     private GridPane boardGrid;
//     private TextArea output;
//     private Button rollDiceBtn, backBtn;

//     private Label diceDisplay;
//     private Label playerNameLabel;   // <<--- NEW

//     private String playerName;        // <<--- NEW

//     private int N;
//     private Map<Integer, Integer> snakes = new HashMap<>();
//     private Map<Integer, Integer> ladders = new HashMap<>();
//     private Map<Integer, StackPane> cellMap = new HashMap<>();
//     private Label playerEmoji;

//     private int playerPosition = 1;
//     private Random rand = new Random();

//     // <<--- constructor updated
//     public SnakeLadderUI(Stage stage, String playerName) {
//         this.stage = stage;
//         this.playerName = playerName;

//         root = new BorderPane();
//         root.setStyle("-fx-background-color: linear-gradient(to bottom, #74ebd5, #9face6);");

//         // Output Log
//         output = new TextArea();
//         output.setEditable(false);
//         output.setPrefHeight(140);
//         output.setStyle("-fx-font-size: 15px; -fx-background-radius: 12px; -fx-text-fill: #333;");

//         // Buttons
//         rollDiceBtn = new Button("🎲 Roll Dice");
//         rollDiceBtn.setFont(Font.font(18));
//         rollDiceBtn.setStyle(buttonStyle());
//         rollDiceBtn.setOnAction(e -> rollDice());

//         backBtn = new Button("⬅ Back");
//         backBtn.setFont(Font.font(16));
//         backBtn.setStyle(buttonStyle());
//         backBtn.setOnAction(e -> MenuScreen.open(stage));

//         // Player Name Label
//         playerNameLabel = new Label("👤 Player: " + playerName);
//         playerNameLabel.setFont(Font.font(24));
//         playerNameLabel.setStyle("""
//                 -fx-background-color: white;
//                 -fx-padding: 10px 25px;
//                 -fx-background-radius: 20px;
//                 -fx-font-weight: bold;
//                 -fx-text-fill: #333;
//                 -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0, 0, 2);
//                 """);

//         // Dice Display Box
//         diceDisplay = new Label("🎲 -");
//         diceDisplay.setFont(Font.font(30));
//         diceDisplay.setStyle("""
//                 -fx-background-color: white;
//                 -fx-padding: 12px 25px;
//                 -fx-background-radius: 20px;
//                 -fx-font-weight: bold;
//                 -fx-text-fill: #333;
//                 -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 2);
//                 """);

//         VBox diceBox = new VBox(diceDisplay);
//         diceBox.setAlignment(Pos.CENTER);

//         // Buttons Row
//         HBox buttonRow = new HBox(20, rollDiceBtn, backBtn);
//         buttonRow.setAlignment(Pos.CENTER);

//         // NEW >>> Combine Player Name + Dice + Buttons
//         VBox topArea = new VBox(15, playerNameLabel, diceBox, buttonRow);
//         topArea.setAlignment(Pos.CENTER);
//         topArea.setStyle("-fx-padding: 10px;");

//         root.setTop(topArea);
//         root.setBottom(output);

//         generateBoard();
//         drawBoard();

//         Scene scene = new Scene(root, 850, 900);
//         stage.setScene(scene);
//         stage.setTitle("🐍 Snake & Ladder 🪜");
//         stage.show();
//     }

//     private String buttonStyle() {
//         return """
//                -fx-background-color: #ffffff;
//                -fx-background-radius: 15px;
//                -fx-padding: 10px 18px;
//                -fx-text-fill: #333;
//                -fx-font-weight: bold;
//                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 0, 2);
//                """;
//     }

//     private void generateBoard() {
//         N = 8;
//         int numSnakes = N - 2;
//         int numLadders = N - 2;

//         snakes.clear();
//         ladders.clear();

//         while (snakes.size() < numSnakes) {
//             int start = 2 + rand.nextInt(N * N - 2);
//             int end = 1 + rand.nextInt(start - 1);
//             snakes.put(start, end);
//         }

//         while (ladders.size() < numLadders) {
//             int start = 1 + rand.nextInt(N * N - 2);
//             int end = start + 1 + rand.nextInt(N * N - start - 1);
//             ladders.put(start, end);
//         }

//         output.setText("✨ New Board Generated!\nClick 🎲 Roll Dice to start.");
//     }

//     private void drawBoard() {
//         boardGrid = new GridPane();
//         boardGrid.setAlignment(Pos.CENTER);
//         boardGrid.setHgap(3);
//         boardGrid.setVgap(3);
//         boardGrid.setStyle("-fx-padding: 15px;");

//         int num = 1;
//         boolean leftToRight = true;
//         cellMap.clear();

//         for (int r = N - 1; r >= 0; r--) {
//             for (int c = 0; c < N; c++) {

//                 int col = leftToRight ? c : (N - 1 - c);

//                 StackPane cell = new StackPane();
//                 cell.setPrefSize(90, 90);
//                 cell.setStyle("""
//                         -fx-border-radius: 12px;
//                         -fx-background-radius: 12px;
//                         -fx-background-color: #ffffffAA;
//                         -fx-border-color: #444;
//                         -fx-border-width: 1px;
//                         """);

//                 Label lbl = new Label(String.valueOf(num));
//                 lbl.setFont(Font.font(16));
//                 lbl.setStyle("-fx-font-weight: bold;");

//                 cell.getChildren().add(lbl);

//                 boardGrid.add(cell, col, r);
//                 cellMap.put(num, cell);
//                 num++;
//             }
//             leftToRight = !leftToRight;
//         }

//         // Player Emoji 😀
//         playerEmoji = new Label("😀");
//         playerEmoji.setFont(Font.font(32));
//         cellMap.get(1).getChildren().add(playerEmoji);

//         // Draw Snakes 🐍
//         for (var e : snakes.entrySet()) {
//             Label snake = new Label("🐍→" + e.getValue());
//             snake.setFont(Font.font(18));
//             snake.setTextFill(Color.DARKRED);
//             StackPane.setAlignment(snake, Pos.TOP_LEFT);
//             cellMap.get(e.getKey()).getChildren().add(snake);
//         }

//         // Draw Ladders 🪜
//         for (var e : ladders.entrySet()) {
//             Label ladder = new Label("🪜→" + e.getValue());
//             ladder.setFont(Font.font(18));
//             ladder.setTextFill(Color.DARKGREEN);
//             StackPane.setAlignment(ladder, Pos.BOTTOM_RIGHT);
//             cellMap.get(e.getKey()).getChildren().add(ladder);
//         }

//         root.setCenter(boardGrid);
//     }

//     private void rollDice() {
//         int dice = 1 + rand.nextInt(6);

//         // Update the big dice display 🎲
//         diceDisplay.setText("🎲 " + dice);

//         output.appendText("\nDice rolled: " + dice);

//         int nextPos = playerPosition + dice;
//         if (nextPos > N * N) nextPos = playerPosition;

//         if (snakes.containsKey(nextPos)) {
//             nextPos = snakes.get(nextPos);
//             output.appendText(" → 🐍 Snake! Go to " + nextPos);
//         } else if (ladders.containsKey(nextPos)) {
//             nextPos = ladders.get(nextPos);
//             output.appendText(" → 🪜 Ladder! Go to " + nextPos);
//         }

//         animateMove(playerPosition, nextPos);
//         playerPosition = nextPos;

//         if (playerPosition == N * N) {
//             output.appendText("\n🎉 YOU WIN!");
//         }
//     }

//     private void animateMove(int from, int to) {
//         StackPane oldCell = cellMap.get(from);
//         StackPane newCell = cellMap.get(to);

//         oldCell.getChildren().remove(playerEmoji);
//         newCell.getChildren().add(playerEmoji);

//         TranslateTransition tt = new TranslateTransition(Duration.millis(200));
//         tt.setNode(playerEmoji);
//         tt.setFromY(-5);
//         tt.setToY(5);
//         tt.play();
//     }

//     // overload open() to keep old usage
//     public static void open(Stage stage) {
//         new SnakeLadderUI(stage, "Player 1");
//     }

//     // new version to pass actual name
//     public static void open(Stage stage, String playerName) {
//         new SnakeLadderUI(stage, playerName);
//     }
// }


// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
package com.snakeandladder.ui;

import com.snakeandladder.db.SnakeLadderDAO;
import com.snakeandladder.logic.BoardGenerator;
import com.snakeandladder.logic.SnakeLadderBFS;
import com.snakeandladder.logic.SnakeLadderDP;
import com.snakeandladder.models.SnakeLadderSolution;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SnakeLadderUI {
   private Stage stage;
   private BorderPane root;
   private GridPane boardGrid;
   private TextArea outputArea;
   private int N = 9;
   private SnakeLadderSolution gameData;
   private Map<Integer, Integer> currentSnakes;
   private Map<Integer, Integer> currentLadders;
   private Map<Integer, Integer> reverseSnakes = new HashMap();
   private Map<Integer, Integer> reverseLadders = new HashMap();
   private static final String FONT_FAMILY = "Montserrat";
   private static final double SCENE_WIDTH = 1250.0;
   private static final double SCENE_HEIGHT = 850.0;
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
   private static final String STYLE_CARD = "-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);";
   private static final String STYLE_GAME_BUTTON_PRIMARY = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 18 45; -fx-background-radius: 12;";
   private static final String STYLE_TITLE_1ST_PAGE = "-fx-font-size: 40px; -fx-font-weight: 800; -fx-text-fill: #2c3e50;";
   private static final String STYLE_SUBTITLE_1ST_PAGE = "-fx-font-size: 18px; -fx-text-fill: #555; -fx-padding: 0 0 20 0;";
   private static final String STYLE_BUTTON_1ST_PAGE = "-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 30; -fx-background-radius: 10;";
   private static final String STYLE_INPUT_1ST_PAGE = "-fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #ddd; -fx-border-radius: 8;";

   public SnakeLadderUI(Stage stage) {
      this.stage = stage;
      this.root = new BorderPane();
      this.root.setStyle("-fx-background-color: #ecf0f1; -fx-font-family: Montserrat;");
      this.outputArea = new TextArea();
      this.outputArea.setEditable(false);
      this.outputArea.setPrefHeight(100.0);
      this.outputArea.setFont(Font.font("Monospaced", 10.0));
      this.outputArea.setStyle("-fx-control-inner-background:#1f2937; -fx-text-fill: #99f6e4; -fx-background-radius: 0; -fx-border-width: 0;");
      this.root.setBottom(this.outputArea);
      this.showInputScreen();
      Scene scene = new Scene(this.root, 1250.0, 850.0);
      stage.setScene(scene);
      stage.setTitle("Snake and Ladder Solver");
      stage.show();
   }

   private void showInputScreen() {
      this.root.setCenter((Node)null);
      this.root.setRight((Node)null);
      Label mainTitle = new Label("\ud83d\udc0d Snake and Ladder \ud83e\ude9c");
      mainTitle.setStyle("-fx-font-size: 40px; -fx-font-weight: 800; -fx-text-fill: #2c3e50;");
      Label subTitle = new Label("Determine the minimum throws required to win on a random board.");
      subTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: #555; -fx-padding: 0 0 20 0;");
      VBox inputCard = new VBox(25.0);
      inputCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);");
      inputCard.setAlignment(Pos.TOP_LEFT);
      inputCard.setMaxWidth(450.0);
      Label nameLabel = this.createHeaderLabel("Player Name", "#333");
      nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
      TextField nameInput = new TextField();
      nameInput.setPromptText("Enter your name (e.g., Alice)");
      nameInput.setText("Sachala");
      nameInput.setStyle("-fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #ddd; -fx-border-radius: 8;");
      VBox nameControl = new VBox(5.0, new Node[]{nameLabel, nameInput});
      Label sizeTitle = this.createHeaderLabel("Board Size (N x N)", "#333");
      sizeTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
      TextField sizeInput = new TextField(String.valueOf(this.N));
      sizeInput.setPromptText("Enter N (6 to 12)");
      sizeInput.setMaxWidth(150.0);
      sizeInput.setStyle("-fx-padding: 10; -fx-background-radius: 8; -fx-border-color: #ddd; -fx-border-radius: 8;");
      int var10002 = this.N * this.N;
      Label cellsLabel = new Label("Total Cells: " + var10002);
      cellsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #777;");
      sizeInput.textProperty().addListener((obs, oldVal, newVal) -> {
         try {
            int newN = Integer.parseInt(newVal);
            cellsLabel.setText("Total Cells: " + newN * newN);
         } catch (NumberFormatException var5) {
            cellsLabel.setText("Total Cells: Invalid Input");
         }

      });
      HBox sizeBox = new HBox(15.0, new Node[]{sizeInput, cellsLabel});
      sizeBox.setAlignment(Pos.CENTER_LEFT);
      VBox sizeControl = new VBox(5.0, new Node[]{sizeTitle, sizeBox});
      Button startBtn = new Button("Generate & Start Challenge");
      startBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 30; -fx-background-radius: 10;");
      startBtn.setPrefHeight(50.0);
      startBtn.setMaxWidth(Double.MAX_VALUE);
      startBtn.setOnAction((e) -> {
         if (nameInput.getText().trim().isEmpty()) {
            this.showAlert("Input Error", "Please enter your name.");
         } else {
            try {
               int inputN = Integer.parseInt(sizeInput.getText().trim());
               if (inputN < 6 || inputN > 12) {
                  this.showAlert("Input Error", "Board size N must be between 6 and 12.");
                  return;
               }

               this.N = inputN;
               this.gameData = new SnakeLadderSolution();
               this.gameData.setPlayerName(nameInput.getText().trim());
               this.startGameRound();
            } catch (NumberFormatException var5) {
               this.showAlert("Input Error", "Please enter a valid number for board size N.");
            }

         }
      });
      inputCard.getChildren().addAll(new Node[]{nameControl, sizeControl, startBtn});
      VBox contentLayout = new VBox(20.0, new Node[]{mainTitle, subTitle, inputCard});
      contentLayout.setAlignment(Pos.CENTER);
      contentLayout.setPadding(new Insets(50.0));
      ScrollPane scrollPane = new ScrollPane(contentLayout);
      scrollPane.setFitToWidth(true);
      scrollPane.setFitToHeight(true);
      scrollPane.setStyle("-fx-background-color: transparent;");
      this.root.setCenter(scrollPane);
   }

   private void startGameRound() {
      BoardGenerator generator = new BoardGenerator(this.N);
      this.currentSnakes = generator.generateSnakes();
      this.currentLadders = generator.generateLadders();
      this.reverseSnakes = (Map)this.currentSnakes.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
      this.reverseLadders = (Map)this.currentLadders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
      SnakeLadderBFS bfsSolver = new SnakeLadderBFS(this.N, this.currentSnakes, this.currentLadders);
      SnakeLadderDP dpSolver = new SnakeLadderDP(this.N, this.currentSnakes, this.currentLadders);
      Map<String, Long> bfsResult = bfsSolver.solveAndMeasureTime();
      Map<String, Long> dpResult = dpSolver.solveAndMeasureTime();
      int bfsThrows = ((Long)bfsResult.get("minThrows")).intValue();
      int dpThrows = ((Long)dpResult.get("minThrows")).intValue();
      int correctThrows = bfsThrows >= 0 && dpThrows >= 0 ? Math.min(bfsThrows, dpThrows) : Math.max(bfsThrows, dpThrows);
      if (correctThrows < 0) {
         correctThrows = Math.max(bfsThrows, dpThrows);
      }

      List<Integer> optimalPath = bfsSolver.getOptimalPath();
      this.gameData.setBoardSize(this.N);
      this.gameData.setSnakes(this.currentSnakes);
      this.gameData.setLadders(this.currentLadders);
      this.gameData.setCorrectMinThrows(correctThrows);
      this.gameData.setAlgo1TimeNanos((Long)bfsResult.get("timeNanos"));
      this.gameData.setAlgo2TimeNanos((Long)dpResult.get("timeNanos"));
      this.gameData.setOptimalPath(optimalPath);
      TextArea var10000 = this.outputArea;
      double var10001 = (double)this.gameData.getAlgo1TimeNanos() / 1000000.0;
      var10000.appendText("Algorithms Solved: BFS Time: " + var10001 + "ms, DP Time: " + (double)this.gameData.getAlgo2TimeNanos() / 1000000.0 + "ms\n");
      this.outputArea.appendText("Correct minimum throws: " + correctThrows + ". Player: " + this.gameData.getPlayerName() + "\n");
      this.drawBoard((List)null);
      this.startGameScreen(correctThrows);
   }

   private void startGameScreen(int correctThrows) {
      VBox challengeCard = new VBox(20.0);
      challengeCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);");
      challengeCard.setMaxWidth(300.0);
      Label challengeTitle = new Label("Your Challenge");
      challengeTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
      int var10002 = this.N * this.N;
      Label question = new Label("What is the minimum number of throws required to reach cell " + var10002 + "?");
      question.setWrapText(true);
      question.setStyle("-fx-font-size: 14px;");
      List<Integer> choices = this.generateChoices(correctThrows, 3);
      ToggleGroup group = new ToggleGroup();
      VBox choiceBox = new VBox(15.0);
      choiceBox.setPadding(new Insets(10.0, 0.0, 10.0, 0.0));
      Iterator var9 = choices.iterator();

      while(var9.hasNext()) {
         int choice = (Integer)var9.next();
         ToggleButton choiceBtn = new ToggleButton(String.valueOf(choice) + " Moves");
         choiceBtn.setPrefWidth(250.0);
         choiceBtn.setToggleGroup(group);
         choiceBtn.setUserData(choice);
         choiceBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #333; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
         choiceBtn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
               choiceBtn.setStyle("-fx-background-color: #dbeaff; -fx-border-color: #3498db; -fx-border-width: 2px; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20; -fx-text-fill: #2c3e50;");
            } else {
               choiceBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #333; -fx-font-size: 16px; -fx-background-radius: 8; -fx-padding: 10 20;");
            }

         });
         choiceBox.getChildren().add(choiceBtn);
      }

      Button submitBtn = new Button("Submit Guess");
      submitBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 18 45; -fx-background-radius: 12;");
      submitBtn.setPrefWidth(250.0);
      submitBtn.setOnAction((e) -> {
         ToggleButton selected = (ToggleButton)group.getSelectedToggle();
         if (selected == null) {
            this.showAlert("Submission Error", "Please select one of the choices.");
         } else {
            int playerGuess = (Integer)selected.getUserData();
            this.gameData.setPlayerAnswer(playerGuess);
            this.showResultScreen(correctThrows);
         }
      });
      challengeCard.getChildren().addAll(new Node[]{challengeTitle, question, choiceBox, submitBtn});
      challengeCard.setAlignment(Pos.TOP_CENTER);
      VBox historyTable = this.createHistoryAnalyticsTable();
      historyTable.setMaxWidth(550.0);
      VBox leftContent = new VBox(30.0, new Node[]{this.boardGrid, historyTable});
      leftContent.setAlignment(Pos.CENTER);
      leftContent.setPadding(new Insets(20.0));
      ScrollPane scrollPane = new ScrollPane(leftContent);
      scrollPane.setFitToWidth(true);
      scrollPane.setStyle("-fx-background-color: transparent;");
      this.root.setCenter(scrollPane);
      this.root.setRight(new VBox(20.0, new Node[]{challengeCard}));
      BorderPane.setMargin(this.root.getRight(), new Insets(20.0));
   }

   private void showResultScreen(int correctThrows) {
      boolean isCorrect = this.gameData.getPlayerAnswer() == correctThrows;
      if (this.gameData.getPlayerAnswer() >= 0) {
         try {
            SnakeLadderDAO dao = new SnakeLadderDAO();
            dao.saveSolution(this.gameData);
         } catch (RuntimeException | SQLException var20) {
            this.outputArea.appendText("Database/File Persistence Error: " + var20.getMessage() + "\n");
         }
      }

      VBox resultCard = new VBox(30.0);
      resultCard.setPadding(new Insets(40.0));
      resultCard.setAlignment(Pos.TOP_CENTER);
      resultCard.setMaxWidth(800.0);
      resultCard.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);");
      VBox resultBanner = new VBox(5.0);
      resultBanner.setAlignment(Pos.CENTER);
      String bannerStyle;
      String resultText;
      if (isCorrect) {
         bannerStyle = "-fx-background-color: #1abc9c; -fx-background-radius: 5;";
         resultText = "✅ Correct! Data Saved.";
         this.outputArea.appendText("\n✅ CORRECT. Data saved.\n");
      } else {
         bannerStyle = "-fx-background-color: #e74c3c; -fx-background-radius: 5;";
         resultText = "❌ Incorrect";
         this.outputArea.appendText("\n❌ INCORRECT. Correct answer was " + correctThrows + ".\n");
      }

      Label resultLabel = new Label(resultText);
      resultLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
      Label moveLabel = new Label("The minimum moves required is " + correctThrows + ".");
      moveLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
      resultBanner.getChildren().addAll(new Node[]{resultLabel, moveLabel});
      resultBanner.setStyle(bannerStyle + "-fx-padding: 15;");
      VBox pathCard = new VBox(15.0);
      pathCard.setAlignment(Pos.CENTER);
      int var10002 = this.gameData.getOptimalPath().size() > 0 ? this.gameData.getOptimalPath().size() - 1 : 0;
      Label pathTitle = new Label("Optimal Path Solution (Total Moves: " + var10002 + ")");
      pathTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
      HBox pathHBox = this.createOptimalPathVisualization(this.gameData.getOptimalPath());
      pathCard.getChildren().addAll(new Node[]{pathTitle, pathHBox});
      HBox algoCard = new HBox(40.0);
      algoCard.setAlignment(Pos.CENTER);
      VBox bfsTime = this.createAlgoTimeBox("ALGORITHM 1: BFS", (double)this.gameData.getAlgo1TimeNanos() / 1000000.0);
      VBox dpTime = this.createAlgoTimeBox("ALGORITHM 2: DP", (double)this.gameData.getAlgo2TimeNanos() / 1000000.0);
      algoCard.getChildren().addAll(new Node[]{bfsTime, dpTime});
      Button playAgainBtn = new Button("Play Again");
      playAgainBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 18 45; -fx-background-radius: 12;");
      playAgainBtn.setPrefWidth(200.0);
      playAgainBtn.setOnAction((e) -> {
         this.showInputScreen();
      });
      resultCard.getChildren().addAll(new Node[]{resultBanner, algoCard, pathCard, playAgainBtn});
      VBox historyTable = this.createHistoryAnalyticsTable();
      historyTable.setMaxWidth(700.0);
      this.drawBoard(this.gameData.getOptimalPath());
      VBox boardContainer = new VBox(new Node[]{this.boardGrid});
      boardContainer.setAlignment(Pos.CENTER);
      VBox finalContent = new VBox(30.0, new Node[]{resultCard, boardContainer, historyTable});
      finalContent.setAlignment(Pos.CENTER);
      finalContent.setPadding(new Insets(20.0));
      ScrollPane scrollPane = new ScrollPane(finalContent);
      scrollPane.setFitToWidth(true);
      scrollPane.setStyle("-fx-background-color: transparent;");
      this.root.setCenter(scrollPane);
      this.root.setRight((Node)null);
   }

   private void drawBoard(List<Integer> highlightPath) {
      this.boardGrid = new GridPane();
      this.boardGrid.setAlignment(Pos.CENTER);
      this.boardGrid.setHgap(1.0);
      this.boardGrid.setVgap(1.0);
      this.boardGrid.setStyle("-fx-background-color: #1a242f; -fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 0);");
      int num = 1;
      boolean leftToRight = true;
      double cellSize = 650.0 / (double)this.N;

      for(int r = this.N - 1; r >= 0; --r) {
         for(int c = 0; c < this.N; ++c) {
            int col = leftToRight ? c : this.N - 1 - c;
            StackPane cell = new StackPane();
            cell.setPrefSize(cellSize, cellSize);
            cell.setPadding(new Insets(2.0));
            String cellStyle = "-fx-border-width: 0; -fx-background-radius: 0;";
            boolean isLight = r % 2 == 0 ? c % 2 == 0 : c % 2 != 0;
            String baseColor = isLight ? "#4a627a" : "#34495e";
            String cellSpecificStyle = "-fx-background-color: " + baseColor + ";";
            Label lblNum = new Label(String.valueOf(num));
            lblNum.setFont(Font.font("Montserrat", 24.0 / ((double)this.N / 4.0)));
            lblNum.setStyle("-fx-font-weight: 900; -fx-text-fill: #ecf0f1;");
            StackPane.setAlignment(lblNum, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(lblNum, new Insets(5.0));
            cell.getChildren().add(lblNum);
            if (this.currentSnakes.containsKey(num) || this.currentLadders.containsKey(num)) {
               VBox iconContainer = new VBox(5.0);
               iconContainer.setAlignment(Pos.TOP_LEFT);
               StackPane.setMargin(iconContainer, new Insets(5.0));
               Label transferLbl = new Label();
               int destination = 0;
               String color = "";
               String tooltipText = "";
               
               if (this.currentSnakes.containsKey(num)) {
                  destination = (Integer)this.currentSnakes.get(num);
                  color = "#27ae60";
                  transferLbl.setText("\ud83d\udc0d↓ TO " + destination);
                  tooltipText = "Snake Head: " + num + " down to " + destination;
               } else if (this.currentLadders.containsKey(num)) {
                  destination = (Integer)this.currentLadders.get(num);
                  color = "#ffc300";
                  transferLbl.setText("\ud83e\ude9c↑ TO " + destination);
                  tooltipText = "Ladder Base: " + num + " up to " + destination;
               }

               transferLbl.setFont(Font.font("Montserrat", 10.0 + (double)(9 - this.N) / 2.0));
               transferLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a242f; -fx-background-color: " + color + "; -fx-padding: 2 5; -fx-background-radius: 4;");
               iconContainer.getChildren().add(transferLbl);
               cell.getChildren().add(iconContainer);
               Tooltip.install(cell, new Tooltip(tooltipText));
            }

            if (highlightPath != null && highlightPath.contains(num)) {
               cellSpecificStyle = cellSpecificStyle + "-fx-border-width: 4px; -fx-border-color: #f1c40f; -fx-effect: dropshadow(gaussian, rgba(241,196,15,0.8), 5, 0, 0, 0);";
            }

            cell.setStyle(cellStyle + cellSpecificStyle);
            Label goalLbl;
            if (num == 1) {
               goalLbl = new Label("START");
               goalLbl.setFont(Font.font("Montserrat", 14.0));
               goalLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: #1a242f; -fx-background-color: #1abc9c; -fx-padding: 5 10; -fx-background-radius: 5;");
               StackPane.setAlignment(goalLbl, Pos.CENTER);
               cell.getChildren().add(goalLbl);
               cell.setStyle(cellStyle + "-fx-background-color: #89e7d9;");
            }

            if (num == this.N * this.N) {
               goalLbl = new Label("GOAL");
               goalLbl.setFont(Font.font("Montserrat", 16.0));
               goalLbl.setStyle("-fx-font-weight: 800; -fx-text-fill: white; -fx-background-color: #e74c3c; -fx-padding: 5 10; -fx-background-radius: 5;");
               StackPane.setAlignment(goalLbl, Pos.CENTER);
               cell.getChildren().add(goalLbl);
               cell.setStyle(cellStyle + "-fx-background-color: #f7a29a;");
            }

            this.boardGrid.add(cell, col, r);
            ++num;
         }

         leftToRight = !leftToRight;
      }

      VBox boardContainer = new VBox(new Node[]{this.boardGrid});
      boardContainer.setAlignment(Pos.CENTER);
      this.root.setCenter(boardContainer);
      BorderPane.setMargin(boardContainer, new Insets(10.0));
   }

   private VBox createAlgoTimeBox(String title, double timeMs) {
      VBox box = new VBox(5.0);
      box.setAlignment(Pos.CENTER);
      Label titleLbl = new Label(title);
      titleLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #3498db;");
      Label timeLbl = new Label(String.format("%.4f ms", timeMs));
      timeLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");
      Label executionLbl = new Label("Execution Time");
      executionLbl.setStyle("-fx-font-size: 10px; -fx-text-fill: #999;");
      box.getChildren().addAll(new Node[]{titleLbl, timeLbl, executionLbl});
      box.setPrefWidth(200.0);
      box.setStyle("-fx-border-color: #eee; -fx-border-radius: 8; -fx-padding: 15; -fx-background-color: #f9f9f9;");
      return box;
   }

   private VBox createHistoryAnalyticsTable() {
      VBox container = new VBox(10.0);
      container.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 30; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 5);");
      container.setPadding(new Insets(15.0));
      container.setMaxWidth(Double.MAX_VALUE);
      Label title = new Label("Game History & Analytics");
      title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333;");
      container.getChildren().add(title);
      GridPane table = new GridPane();
      table.setHgap(15.0);
      table.setVgap(8.0);
      table.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
      ColumnConstraints col1 = new ColumnConstraints();
      col1.setMinWidth(60.0);
      ColumnConstraints col2 = new ColumnConstraints();
      col2.setMinWidth(80.0);
      ColumnConstraints col3 = new ColumnConstraints();
      col3.setMinWidth(80.0);
      ColumnConstraints col4 = new ColumnConstraints();
      col4.setMinWidth(80.0);
      ColumnConstraints col5 = new ColumnConstraints();
      col5.setMinWidth(100.0);
      ColumnConstraints col6 = new ColumnConstraints();
      col6.setMinWidth(100.0);
      table.getColumnConstraints().addAll(new ColumnConstraints[]{col1, col2, col3, col4, col5, col6});
      String headerStyle = "-fx-font-weight: bold; -fx-text-fill: #555; -fx-font-size: 12px; -fx-border-width: 0 0 1 0; -fx-border-color: #ccc; -fx-padding: 0 0 5 0;";
      Label timeHeader = new Label("#");
      timeHeader.setStyle(headerStyle);
      table.add(timeHeader, 0, 0);
      Label playerHeader = new Label("PLAYER");
      playerHeader.setStyle(headerStyle);
      table.add(playerHeader, 1, 0);
      Label boardHeader = new Label("BOARD(N)");
      boardHeader.setStyle(headerStyle);
      table.add(boardHeader, 2, 0);
      Label resultHeader = new Label("RESULT");
      resultHeader.setStyle(headerStyle);
      table.add(resultHeader, 3, 0);
      Label bfsHeader = new Label("BFS TIME(ms)");
      bfsHeader.setStyle(headerStyle);
      table.add(bfsHeader, 4, 0);
      Label dpHeader = new Label("DP TIME(ms)");
      dpHeader.setStyle(headerStyle);
      table.add(dpHeader, 5, 0);

      try {
         SnakeLadderDAO dao = new SnakeLadderDAO();
         List<SnakeLadderSolution> history = dao.getAllSolutions(5);

         for(int i = 0; i < history.size(); ++i) {
            SnakeLadderSolution entry = (SnakeLadderSolution)history.get(i);
            int row = i + 1;
            String resultText = entry.getPlayerAnswer() == entry.getCorrectMinThrows() ? "WIN" : "LOSS";
            String resultColor = resultText.equals("WIN") ? "#1abc9c" : "#e74c3c";
            String entryNumStr = String.valueOf(history.size() - i);
            String cellStyle = "-fx-font-size: 12px; -fx-padding: 2 0;";
            table.add(new Label(entryNumStr), 0, row);
            table.add(new Label(entry.getPlayerName()), 1, row);
            int var10003 = entry.getBoardSize();
            table.add(new Label("" + var10003 + "x" + entry.getBoardSize()), 2, row);
            Label resultLbl = new Label(resultText);
            resultLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: " + resultColor + ";" + cellStyle);
            table.add(resultLbl, 3, row);
            Label bfsTimeLbl = new Label(String.format("%.2f", (double)entry.getAlgo1TimeNanos() / 1000000.0));
            bfsTimeLbl.setStyle(cellStyle);
            table.add(bfsTimeLbl, 4, row);
            Label dpTimeLbl = new Label(String.format("%.2f", (double)entry.getAlgo2TimeNanos() / 1000000.0));
            dpTimeLbl.setStyle(cellStyle);
            table.add(dpTimeLbl, 5, row);
         }
      } catch (RuntimeException | SQLException var29) {
         Label errorLabel = new Label("Could not load history (DAO): " + var29.getMessage());
         errorLabel.setStyle("-fx-text-fill: #e74c3c;");
         container.getChildren().add(errorLabel);
         this.outputArea.appendText("Error loading history: " + var29.getMessage() + "\n");
      }

      container.getChildren().add(table);
      return container;
   }

   private HBox createOptimalPathVisualization(List<Integer> path) {
      HBox pathBox = new HBox(5.0);
      pathBox.setAlignment(Pos.CENTER);
      pathBox.setPadding(new Insets(5.0));
      pathBox.setSpacing(10.0);
      if (path != null && !path.isEmpty()) {
         int displayLimit = 10;

         for(int i = 0; i < path.size(); ++i) {
            boolean isGoal = i == path.size() - 1;
            boolean isStart = i == 0;
            if (i >= displayLimit && !isGoal) {
               if (i != path.size() - 2 && i == displayLimit) {
                  Label dots = new Label("...");
                  dots.setStyle("-fx-font-size: 20px; -fx-text-fill: #999;");
                  pathBox.getChildren().add(dots);
               }

               if (i < path.size() - 1) {
                  continue;
               }
            }

            int cellNum = (Integer)path.get(i);
            String arrow = "";
            String cellStyle = "-fx-background-radius: 20; -fx-padding: 8 12; -fx-text-fill: white; -fx-font-weight: bold;";
            String cellColor;
            if (isStart) {
               cellColor = "#1abc9c";
               arrow = "";
            } else if (isGoal) {
               cellColor = "#e74c3c";
               arrow = "➡️";
            } else {
               cellColor = "#3498db";
               arrow = "➡️";
            }

            Label cellLabel;
            if (!isStart) {
               cellLabel = new Label(arrow);
               cellLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");
               pathBox.getChildren().add(cellLabel);
            }

            cellLabel = new Label(String.valueOf(cellNum));
            cellLabel.setStyle(cellStyle + "-fx-background-color: " + cellColor + ";");
            pathBox.getChildren().add(cellLabel);
         }

         ScrollPane pathScrollPane = new ScrollPane(pathBox);
         pathScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
         pathScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
         pathScrollPane.setFitToHeight(true);
         pathScrollPane.setMaxHeight(60.0);
         pathScrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #eee; -fx-border-radius: 8;");
         HBox container = new HBox(new Node[]{pathScrollPane});
         container.setAlignment(Pos.CENTER);
         container.setMaxWidth(Double.MAX_VALUE);
         return container;
      } else {
         Label errorLabel = new Label("Path not available (Board may be blocked).");
         pathBox.getChildren().add(errorLabel);
         return new HBox(new Node[]{pathBox});
      }
   }

   private Label createHeaderLabel(String text, String color) {
      Label label = new Label(text);
      label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: " + color + ";");
      return label;
   }

   private List<Integer> generateChoices(int correctThrows, int limit) {
      Set<Integer> choices = new HashSet();
      choices.add(correctThrows);
      if (correctThrows > 2) {
         choices.add(correctThrows - 1);
      }

      choices.add(correctThrows + 1);
      choices.add(correctThrows + 2);

      while(choices.size() < limit + 1) {
         choices.add((Integer)Collections.max(choices) + 1);
      }

      List<Integer> sortedChoices = new ArrayList(choices);
      Collections.sort(sortedChoices);
      return (List)sortedChoices.stream().filter((c) -> {
         return c > 0;
      }).limit((long)limit).collect(Collectors.toList());
   }

   private void showAlert(String title, String message) {
      Alert alert = new Alert(AlertType.WARNING);
      alert.setTitle(title);
      alert.setHeaderText((String)null);
      alert.setContentText(message);
      alert.showAndWait();
   }
}
