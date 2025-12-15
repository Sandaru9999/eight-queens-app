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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class EightQueensScreen {

    private static final int SIZE = 8;
    private StackPane[][] cells = new StackPane[SIZE][SIZE];
    private int[] board = new int[SIZE];
    private VBox root;
    private ToggleGroup algoGroup = new ToggleGroup();
    private final String QUEEN_SYMBOL = "♛"; // Chess queen symbol

    public EightQueensScreen(Stage stage) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3c72, #2a5298);");

        Label title = new Label("Eight Queens Puzzle");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.WHITE);

        GridPane grid = createChessBoard();

        TextField playerName = new TextField();
        playerName.setPromptText("Enter player name");
        playerName.setPrefWidth(250);

        Button checkBtn = createModernButton("Check Validity");
        Button submitBtn = createModernButton("Submit Solution");
        Button viewAllBtn = createModernButton("View All 92 Solutions");
        Button runSequentialBtn = createModernButton("Run Sequential Solver");
        Button runThreadedBtn = createModernButton("Run Threaded Solver");
        Button backBtn = createModernButton("Back to Menu");

        Label timeTakenLabel = new Label(""); // Display algorithm time
        timeTakenLabel.setTextFill(Color.WHITE);
        timeTakenLabel.setFont(Font.font(16));

        TextArea output = new TextArea();
        output.setPrefHeight(250);
        output.setEditable(false);
        output.setWrapText(true);
        output.setStyle("-fx-font-family: 'Segoe UI Symbol'; -fx-font-size: 16px;");

        // Algorithm selection
        RadioButton sequentialOption = new RadioButton("Sequential");
        sequentialOption.setTextFill(Color.WHITE);
        sequentialOption.setToggleGroup(algoGroup);

        RadioButton threadedOption = new RadioButton("Threaded");
        threadedOption.setTextFill(Color.WHITE);
        threadedOption.setToggleGroup(algoGroup);

        VBox algoBox = new VBox(5, sequentialOption, threadedOption);
        algoBox.setPadding(new Insets(5));
        algoBox.setAlignment(Pos.CENTER_LEFT);
        algoBox.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10;");
        algoBox.setPrefWidth(250);

        // ---------------- BUTTON ACTIONS ----------------
        checkBtn.setOnAction(e -> {
            if (isFullSolution()) {
                output.setText(isSolutionValid() ? "✔ Valid Solution! All queens are safe."
                        : "✘ Invalid solution. Some queens can attack each other.");
            } else output.setText("You must place 8 queens first!");
        });

        submitBtn.setOnAction(e -> {
            String name = playerName.getText().trim();
            if (name.isEmpty()) { output.setText("Enter player name first!"); return; }
            if (!isFullSolution()) { output.setText("You must place all 8 queens!"); return; }
            if (!isSolutionValid()) { output.setText("Cannot submit — solution is incorrect."); return; }

            Toggle selectedToggle = algoGroup.getSelectedToggle();
            if (selectedToggle == null) { output.setText("Please select Sequential or Threaded before submitting!"); return; }
            String algorithm = ((RadioButton) selectedToggle).getText();

            String solution = SolutionDAO.boardToString(board);

            if (SolutionDAO.isSolutionRecognized(solution)) {
                output.setText("❌ This solution has already been recognized. Try another."); return;
            }

            // Run algorithm to validate & measure time
            long start = System.currentTimeMillis();
            boolean isValid;
            if (algorithm.equals("Sequential")) isValid = EightQueensSequential.checkSolution(board);
            else isValid = EightQueensThreaded.checkSolution(board);
            long end = System.currentTimeMillis();
            int timeTaken = (int)(end - start);

            if (!isValid) { output.setText("✘ The submitted solution is not valid according to " + algorithm); return; }

            // Save player, solution, algorithm, and time
            SolutionDAO.savePlayerWithAlgorithm(name, solution, algorithm, timeTaken);

            output.setText("✔ Correct solution saved for: " + name +
                    "\nAlgorithm used: " + algorithm +
                    "\n⏱ Time taken: " + timeTaken + " ms");

            algoGroup.selectToggle(null);
        });

        viewAllBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            long seqStart = System.currentTimeMillis();
            List<Solution> seqSolutions = EightQueensSequential.solve();
            long seqEnd = System.currentTimeMillis();
            sb.append("Sequential Solver Solutions:\n");
            for (Solution s : seqSolutions) sb.append(s.toString()).append("\n");
            sb.append("⏱ Time taken: ").append(seqEnd - seqStart).append(" ms\n");

            sb.append("\n-------------------------------------------\n");

            long thStart = System.currentTimeMillis();
            List<Solution> thSolutions = EightQueensThreaded.solve();
            long thEnd = System.currentTimeMillis();
            sb.append("Threaded Solver Solutions:\n");
            for (Solution s : thSolutions) sb.append(s.toString()).append("\n");
            sb.append("⏱ Time taken: ").append(thEnd - thStart).append(" ms\n");

            output.setText(sb.toString());
        });

        runSequentialBtn.setOnAction(e -> {
            long start = System.currentTimeMillis();
            List<Solution> solutions = EightQueensSequential.solve();
            long end = System.currentTimeMillis();
            for (Solution s : solutions)
                SolutionDAO.saveSolution(SolutionDAO.boardToString(s.getPositions()), (int)(end-start));
            timeTakenLabel.setText("Sequential Solver Time: " + (end-start) + " ms");
            output.setText("Sequential Solver completed: " + solutions.size() + " solutions.");
        });

        runThreadedBtn.setOnAction(e -> {
            long start = System.currentTimeMillis();
            List<Solution> solutions = EightQueensThreaded.solve();
            long end = System.currentTimeMillis();
            for (Solution s : solutions)
                SolutionDAO.saveSolution(SolutionDAO.boardToString(s.getPositions()), (int)(end-start));
            timeTakenLabel.setText("Threaded Solver Time: " + (end-start) + " ms");
            output.setText("Threaded Solver completed: " + solutions.size() + " solutions.");
        });

        backBtn.setOnAction(e -> {
            MenuScreen menu = new MenuScreen(stage);
            stage.getScene().setRoot(menu.getView());
        });

        VBox rightPanel = new VBox(10,
                playerName, checkBtn,
                algoBox, submitBtn,
                runSequentialBtn, runThreadedBtn,
                viewAllBtn, timeTakenLabel, backBtn, output
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

    public VBox getView() { return root; }

    private GridPane createChessBoard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(10));

        for (int i = 0; i < SIZE; i++) board[i] = -1;

        for (int row = 0; row <= SIZE; row++) {
            for (int col = 0; col <= SIZE; col++) {
                if (row == 0 && col == 0) continue;

                if (row == 0) {
                    Label colLabel = new Label(String.valueOf(col-1));
                    colLabel.setTextFill(Color.WHITE);
                    colLabel.setFont(Font.font(16));
                    grid.add(colLabel, col, row);
                    continue;
                }
                if (col == 0) {
                    Label rowLabel = new Label(String.valueOf(row-1));
                    rowLabel.setTextFill(Color.WHITE);
                    rowLabel.setFont(Font.font(16));
                    grid.add(rowLabel, col, row);
                    continue;
                }

                int r = row-1, c = col-1;

                StackPane cell = new StackPane();
                cell.setPrefSize(65, 65);

                Rectangle rect = new Rectangle(65, 65);
                rect.setFill((r+c)%2==0 ? Color.web("#f0d9b5") : Color.web("#b58863"));
                rect.setArcWidth(10);
                rect.setArcHeight(10);

                Label queenLabel = new Label("");
                queenLabel.setFont(Font.font("Segoe UI Symbol", 40));
                queenLabel.setTextFill(Color.BLACK);

                cell.getChildren().addAll(rect, queenLabel);
                final int fr = r, fc = c;
                cell.setOnMouseClicked(e -> handleCellClick(fr, fc, queenLabel));
                cells[r][c] = cell;
                grid.add(cell, col, row);
            }
        }

        grid.setStyle("-fx-background-radius:15; -fx-padding:10; -fx-background-color: rgba(255,255,255,0.2);");
        return grid;
    }

    private void handleCellClick(int row,int col, Label queenLabel){
        if(board[row]==col){
            board[row]=-1;
            queenLabel.setText("");
            return;
        }
        if(board[row]!=-1){
            Label oldLabel = (Label)((StackPane)cells[row][board[row]]).getChildren().get(1);
            oldLabel.setText("");
        }
        board[row]=col;
        queenLabel.setText(QUEEN_SYMBOL);
    }

    private boolean isFullSolution() {
        int c=0;
        for(int i:board) if(i!=-1) c++;
        return c==SIZE;
    }

    private boolean isSolutionValid() {
        for(int r1=0;r1<SIZE;r1++)
            for(int r2=r1+1;r2<SIZE;r2++){
                int c1=board[r1], c2=board[r2];
                if(c1==c2 || Math.abs(r1-r2)==Math.abs(c1-c2)) return false;
            }
        return true;
    }

    private Button createModernButton(String text){
        Button btn=new Button(text);
        btn.setFont(Font.font("Verdana",16));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(250);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-radius:20; -fx-background-color: linear-gradient(to right,#ff416c,#ff4b2b);");
        DropShadow shadow=new DropShadow();
        shadow.setColor(Color.rgb(0,0,0,0.3));
        shadow.setRadius(8);
        btn.setEffect(shadow);
        btn.setOnMouseEntered(e->btn.setStyle("-fx-background-radius:20; -fx-background-color: linear-gradient(to right,#ff4b2b,#ff416c);"));
        btn.setOnMouseExited(e->btn.setStyle("-fx-background-radius:20; -fx-background-color: linear-gradient(to right,#ff416c,#ff4b2b);"));
        return btn;
    }
}
