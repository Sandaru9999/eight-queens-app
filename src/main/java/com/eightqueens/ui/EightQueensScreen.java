package com.eightqueens.ui;

import com.eightqueens.logic.EightQueensSequential;
import com.eightqueens.logic.EightQueensThreaded;
import com.eightqueens.models.Solution;
import com.eightqueens.db.SolutionDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class EightQueensScreen {

    private static final int SIZE = 8;
    private Button[][] cells = new Button[SIZE][SIZE];
    private int[] board = new int[SIZE]; // row → col (-1 if empty)
    private VBox root;

    public EightQueensScreen(Stage stage) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        // Title
        Label title = new Label("Eight Queens Puzzle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.WHITE);

        GridPane grid = createChessBoard();

        // Right Panel Controls
        TextField playerName = new TextField();
        playerName.setPromptText("Enter player name");
        playerName.setPrefWidth(250);

        Button checkBtn = createModernButton("Check Validity");
        Button submitBtn = createModernButton("Submit Solution");
        Button viewAllBtn = createModernButton("View All 92 Solutions");
        Button runSequentialBtn = createModernButton("Run Sequential Solver");
        Button runThreadedBtn = createModernButton("Run Threaded Solver");
        Button backBtn = createModernButton("Back to Menu");

        TextArea output = new TextArea();
        output.setPrefHeight(300);
        output.setEditable(false);
        output.setWrapText(true);
        output.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");

        // ---------------- BUTTON ACTIONS ----------------
        checkBtn.setOnAction(e -> {
            if (isFullSolution()) {
                if (isSolutionValid()) {
                    output.setText("✔ Valid Solution! All queens are safe.");
                } else {
                    output.setText("✘ Invalid solution.\nSome queens can attack each other.");
                }
            } else {
                output.setText("You must place 8 queens first!");
            }
        });

        submitBtn.setOnAction(e -> {
            if (playerName.getText().trim().isEmpty()) {
                output.setText("Enter player name first!");
                return;
            }
            if (!isFullSolution()) {
                output.setText("You must place all 8 queens!");
                return;
            }
            if (!isSolutionValid()) {
                output.setText("Cannot submit — solution is incorrect.");
                return;
            }

            // Save player's solution
            String solution = SolutionDAO.boardToString(board);
            int timeTaken = 1234; // TODO: actual timer
            SolutionDAO.savePlayerAndSolution(playerName.getText(), solution, timeTaken);

            output.setText("✔ Player solution saved for: " + playerName.getText());
        });

        viewAllBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();

            long seqStart = System.currentTimeMillis();
            List<Solution> seqSolutions = EightQueensSequential.solve();
            long seqEnd = System.currentTimeMillis();
            int seqDuration = (int) (seqEnd - seqStart);

            sb.append("Sequential Solver:\n");
            sb.append("Time taken: ").append(seqDuration).append(" ms\n");
            sb.append("Solutions:\n");
            for (Solution s : seqSolutions) sb.append(s.toString()).append("\n");

            sb.append("\n-------------------------------------------\n");

            long thStart = System.currentTimeMillis();
            List<Solution> thSolutions = EightQueensThreaded.solve();
            long thEnd = System.currentTimeMillis();
            int thDuration = (int) (thEnd - thStart);

            sb.append("Threaded Solver:\n");
            sb.append("Time taken: ").append(thDuration).append(" ms\n");
            sb.append("Solutions:\n");
            for (Solution s : thSolutions) sb.append(s.toString()).append("\n");

            output.setText(sb.toString());
        });

        runSequentialBtn.setOnAction(e -> {
            long start = System.currentTimeMillis();
            List<Solution> solutions = EightQueensSequential.solve();
            long end = System.currentTimeMillis();
            int duration = (int) (end - start);

            for (Solution s : solutions) {
                SolutionDAO.saveSolution(SolutionDAO.boardToString(s.getPositions()), duration);
            }

            output.setText("Sequential Solver completed: " + solutions.size() + " solutions in " + duration + "ms");
        });

        runThreadedBtn.setOnAction(e -> {
            long start = System.currentTimeMillis();
            List<Solution> solutions = EightQueensThreaded.solve();
            long end = System.currentTimeMillis();
            int duration = (int) (end - start);

            for (Solution s : solutions) {
                SolutionDAO.saveSolution(SolutionDAO.boardToString(s.getPositions()), duration);
            }

            output.setText("Threaded Solver completed: " + solutions.size() + " solutions in " + duration + "ms");
        });

        backBtn.setOnAction(e -> {
            MenuScreen menu = new MenuScreen(stage);
            stage.getScene().setRoot(menu.getView());
        });

        VBox rightPanel = new VBox(15,
                playerName, checkBtn, submitBtn,
                runSequentialBtn, runThreadedBtn,
                viewAllBtn, backBtn, output
        );
        rightPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel.setPrefWidth(400);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-background-radius: 15;");
        rightPanel.setEffect(new DropShadow(10, Color.BLACK));

        HBox gameLayout = new HBox(20, grid, rightPanel);
        gameLayout.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, gameLayout);
    }

    public VBox getView() {
        return root;
    }

    // ---------------- CREATE CHESS BOARD ----------------
    private GridPane createChessBoard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));
        for (int i = 0; i < SIZE; i++) board[i] = -1;

        Color whiteColor = Color.web("#f0d9b5");
        Color blackColor = Color.web("#b58863");

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button cell = new Button();
                cell.setPrefSize(60, 60);
                boolean isWhite = (row + col) % 2 == 0;
                cell.setStyle("-fx-background-color: " + (isWhite ? "#f0d9b5" : "#b58863") + "; -fx-font-size: 32px; -fx-padding: 0;");
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> handleCellClick(r, c));
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
        grid.setStyle("-fx-background-radius: 15; -fx-padding: 10; -fx-background-color: rgba(255,255,255,0.2);");
        return grid;
    }

    private void handleCellClick(int row, int col) {
        if (board[row] == col) {
            board[row] = -1;
            cells[row][col].setText("");
            return;
        }
        if (board[row] != -1) {
            int prevCol = board[row];
            cells[row][prevCol].setText("");
        }
        board[row] = col;
        cells[row][col].setFont(Font.font("Segoe UI Symbol", 32));
        cells[row][col].setText("♛");
    }

    private boolean isFullSolution() {
        int count = 0;
        for (int col : board) if (col != -1) count++;
        return count == SIZE;
    }

    private boolean isSolutionValid() {
        for (int r1 = 0; r1 < SIZE; r1++) {
            for (int r2 = r1 + 1; r2 < SIZE; r2++) {
                int c1 = board[r1];
                int c2 = board[r2];
                if (c1 == c2) return false;
                if (Math.abs(r1 - r2) == Math.abs(c1 - c2)) return false;
            }
        }
        return true;
    }

    // ------------------ CREATE MODERN BUTTON ------------------
    private Button createModernButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Verdana", 16));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(250);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-radius: 20; -fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b);");
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0,0,0,0.3));
        shadow.setRadius(8);
        btn.setEffect(shadow);

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius: 20; -fx-background-color: linear-gradient(to right, #ff4b2b, #ff416c);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius: 20; -fx-background-color: linear-gradient(to right, #ff416c, #ff4b2b);"));
        return btn;
    }
}
