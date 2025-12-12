package com.towerofhanoi.ui;

import com.eightqueens.ui.MenuScreen;
import com.towerofhanoi.db.TowerHanoiDAO;
import com.towerofhanoi.logic.HanoiClassic3Pegs;
import com.towerofhanoi.logic.HanoiClassic4Pegs;
import com.towerofhanoi.models.HanoiSolution;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class HanoiUI {

    private Stage stage;
    private VBox root;
    private TextField diskInput;
    private ChoiceBox<Integer> pegChoice;
    private TextField playerNameInput;
    private HBox pegBoxes;
    private final List<Stack<Rectangle>> pegStacks = new ArrayList<>();
    private final List<VBox> pegVBoxes = new ArrayList<>();
    private Rectangle selectedDisk;
    private int numDisks;
    private int movesCount = 0;
    private Label playerLabel;

    // User move input
    private TextField movesField;
    private TextArea moveSequenceInput;
    private Button submitMovesBtn;

    // Move log
    private TextArea moveLog;

    public HanoiUI(Stage stage) {
        this.stage = stage;

        root = new VBox(18);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #111111, #1a1a1a);");

        // TITLE
        Label title = new Label("🗼 Tower of Hanoi");
        title.setFont(Font.font("Segoe UI", 36));
        title.setTextFill(Color.web("#00FFFF"));
        title.setEffect(new DropShadow(12, Color.BLACK));

        // PLAYER NAME
        playerNameInput = new TextField();
        playerNameInput.setPromptText("Player name");
        playerNameInput.setMaxWidth(200);
        playerNameInput.setStyle(inputStyle());

        playerLabel = new Label("");
        playerLabel.setFont(Font.font(16));
        playerLabel.setTextFill(Color.LIGHTCYAN);

        // DISK INPUT
        diskInput = new TextField();
        diskInput.setPromptText("Disks (5-10)");
        diskInput.setMaxWidth(140);
        diskInput.setStyle(inputStyle());

        // PEG CHOICE
        pegChoice = new ChoiceBox<>();
        pegChoice.getItems().addAll(3, 4);
        pegChoice.setValue(3);
        pegChoice.setStyle("-fx-background-radius: 10; -fx-font-size: 14px;");

        // Buttons
        Button startBtn = modernBtn("Start Game", "#00FFAA", "#00CC88");
        startBtn.setOnAction(e -> startGame());

        Button randomBtn = modernBtn("Random Setup", "#FFAA00", "#CC8800");
        randomBtn.setOnAction(e -> randomGame());

        Button backBtn = modernBtn("Back to Menu", "#FF5555", "#CC3333");
        backBtn.setOnAction(e -> MenuScreen.open(stage));

        HBox controls = new HBox(10, playerNameInput, diskInput, pegChoice, startBtn, randomBtn, backBtn);
        controls.setAlignment(Pos.CENTER);

        // PEG BOXES
        pegBoxes = new HBox(50);
        pegBoxes.setAlignment(Pos.BOTTOM_CENTER);
        pegBoxes.setPrefHeight(340);

        // Move input section
        movesField = new TextField();
        movesField.setPromptText("Number of Moves");
        movesField.setMaxWidth(140);

        moveSequenceInput = new TextArea();
        moveSequenceInput.setPromptText("Enter sequence of moves (e.g., A->C,B->A) or press auto-solve");
        moveSequenceInput.setPrefHeight(80);

        submitMovesBtn = modernBtn("Submit Moves", "#00CCFF", "#0088CC");
        submitMovesBtn.setOnAction(e -> submitUserMoves());

        HBox moveControls = new HBox(10, movesField, submitMovesBtn);
        moveControls.setAlignment(Pos.CENTER);

        // Move log
        moveLog = new TextArea();
        moveLog.setPromptText("Move log / Auto-solve output...");
        moveLog.setPrefHeight(150);
        moveLog.setEditable(false);
        moveLog.setStyle("-fx-control-inner-background: #121212; -fx-text-fill: #00FFAA; -fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        // Auto-solve buttons
        Button solve3RecBtn = modernBtn("3-pegs Rec", "#00FFFF", "#00CCAA");
        solve3RecBtn.setOnAction(e -> autoSolve3Recursive());

        Button solve3IterBtn = modernBtn("3-pegs Iter", "#00FFFF", "#00CCAA");
        solve3IterBtn.setOnAction(e -> autoSolve3Iterative());

        Button solve4FSBtn = modernBtn("4-pegs Frame-Stewart", "#FF00FF", "#CC00CC");
        solve4FSBtn.setOnAction(e -> autoSolve4FrameStewart());

        Button solve4NaiveBtn = modernBtn("4-pegs Naive", "#FF00FF", "#CC00CC");
        solve4NaiveBtn.setOnAction(e -> autoSolve4Naive());

        HBox algos = new HBox(8, solve3RecBtn, solve3IterBtn, solve4FSBtn, solve4NaiveBtn);
        algos.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, playerLabel, controls, pegBoxes, moveSequenceInput, moveControls, moveLog, algos);

        Scene scene = new Scene(root, 1200, 750);
        stage.setScene(scene);
        stage.setTitle("Tower of Hanoi - Modern UI");
        stage.show();
    }

    // --------------------------- GAME LOGIC ---------------------------
    private void startGame() {
        String player = playerNameInput.getText().trim();
        if (player.isEmpty()) { showAlert("Please enter player name."); return; }
        playerLabel.setText("👤 Player: " + player);

        int disks = parseDiskInputOrFallback(5);
        disks = Math.max(5, Math.min(10, disks));
        diskInput.setText(String.valueOf(disks));

        setupGame(disks, pegChoice.getValue());
        moveSequenceInput.clear();
        movesField.clear();
        moveLog.clear();
    }

    private void randomGame() {
        Random rand = new Random();
        int disks = rand.nextInt(6) + 5; // 5..10
        int pegs = rand.nextInt(2) + 3; // 3 or 4
        diskInput.setText(String.valueOf(disks));
        pegChoice.setValue(pegs);
        if (!playerNameInput.getText().trim().isEmpty()) {
            playerLabel.setText("👤 Player: " + playerNameInput.getText().trim());
        }
        setupGame(disks, pegs);
        moveSequenceInput.clear();
        movesField.clear();
        moveLog.clear();
    }

    private void setupGame(int disks, int numPegs) {
        pegBoxes.getChildren().clear();
        pegStacks.clear();
        pegVBoxes.clear();
        selectedDisk = null;
        movesCount = 0;
        numDisks = disks;

        Color[] colors = {Color.web("#FF6B6B"), Color.web("#4ECDC4"), Color.web("#FFD93D"),
                Color.web("#1A535C"), Color.web("#6A0572"), Color.web("#FF9F1C")};

        for (int i = 0; i < numPegs; i++) {
            VBox pegVBox = new VBox(6);
            pegVBox.setAlignment(Pos.BOTTOM_CENTER);
            pegVBox.setPrefSize(140, 300);
            pegVBox.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-background-radius: 12; -fx-padding: 8px;");
            pegVBox.setEffect(new DropShadow(8, Color.rgb(0,0,0,0.45)));

            Rectangle pegStick = new Rectangle(12, 220);
            pegStick.setArcWidth(8);
            pegStick.setArcHeight(8);
            pegStick.setFill(Color.web("#e6e6e6"));
            pegStick.setMouseTransparent(true);

            Stack<Rectangle> stack = new Stack<>();
            pegStacks.add(stack);
            pegVBoxes.add(pegVBox);
            pegVBox.getChildren().add(pegStick);

            if (i == 0) { // source peg with disks
                for (int d = disks; d >= 1; d--) {
                    double width = d * 20 + 40;
                    Rectangle disk = new Rectangle(width, 28, colors[(d - 1) % colors.length]);
                    disk.setArcWidth(16);
                    disk.setArcHeight(16);
                    disk.setEffect(new DropShadow(6, Color.rgb(0,0,0,0.35)));

                    disk.setOnMouseClicked(ev -> selectDisk(disk));

                    stack.push(disk);
                    pegVBox.getChildren().add(disk);
                }
            }

            final int pegIndex = i;
            pegVBox.setOnMouseClicked(ev -> moveDisk(pegIndex));
            pegBoxes.getChildren().add(pegVBox);
        }
    }

    private void selectDisk(Rectangle disk) {
        for (int i = 0; i < pegStacks.size(); i++) {
            Stack<Rectangle> stack = pegStacks.get(i);
            if (stack.contains(disk)) {
                if (stack.peek() != disk) {
                    showAlert("Only the TOP disk can be selected!");
                    return;
                }
                if (selectedDisk != null) {
                    selectedDisk.setStroke(null);
                    selectedDisk.setStrokeWidth(0);
                }
                selectedDisk = disk;
                selectedDisk.setStroke(Color.CYAN);
                selectedDisk.setStrokeWidth(3);
                return;
            }
        }
    }

    private void moveDisk(int targetPeg) {
        if (selectedDisk == null) return;

        int sourcePeg = -1;
        for (int i = 0; i < pegStacks.size(); i++) {
            if (!pegStacks.get(i).isEmpty() && pegStacks.get(i).peek() == selectedDisk) {
                sourcePeg = i;
                break;
            }
        }
        if (sourcePeg == -1 || sourcePeg == targetPeg) {
            if (selectedDisk != null) {
                selectedDisk.setStroke(null);
                selectedDisk.setStrokeWidth(0);
                selectedDisk = null;
            }
            return;
        }

        Stack<Rectangle> targetStack = pegStacks.get(targetPeg);
        if (!targetStack.isEmpty() && targetStack.peek().getWidth() < selectedDisk.getWidth()) {
            showAlert("Cannot place larger disk on smaller disk!");
            return;
        }

        VBox sourceVBox = pegVBoxes.get(sourcePeg);
        VBox targetVBox = pegVBoxes.get(targetPeg);

        pegStacks.get(sourcePeg).pop();
        sourceVBox.getChildren().remove(selectedDisk);
        pegStacks.get(targetPeg).push(selectedDisk);
        targetVBox.getChildren().add(selectedDisk);

        animateDisk(selectedDisk);

        selectedDisk.setStroke(null);
        selectedDisk.setStrokeWidth(0);
        selectedDisk = null;
        movesCount++;
        moveLog.appendText("Moved disk to Peg " + (targetPeg + 1) + "\n");
        checkWin();
    }

    private void animateDisk(Rectangle disk) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), disk);
        tt.setFromY(-20);
        tt.setToY(0);
        tt.play();
    }

    private void checkWin() {
        Stack<Rectangle> lastPeg = pegStacks.get(pegStacks.size() - 1);
        if (lastPeg.size() == numDisks) {
            String player = playerNameInput.getText().trim();
            if (player.isEmpty()) player = "Player";
            showAlert("🏆 Congratulations, " + player + "!\nSolved in " + movesCount + " moves!");
        }
    }

    // ------------------- Submit Moves -------------------
    private void submitUserMoves() {
        String movesSeqRaw = moveSequenceInput.getText().trim();
        String movesCountText = movesField.getText().trim();
        String player = playerNameInput.getText().trim();

        if (player.isEmpty()) { showAlert("Please enter player name."); return; }
        if (movesSeqRaw.isEmpty()) { showAlert("Please enter the sequence of moves."); return; }
        if (movesCountText.isEmpty()) { showAlert("Please enter the number of moves."); return; }

        int movesNumber;
        try { movesNumber = Integer.parseInt(movesCountText); } catch (NumberFormatException e) {
            showAlert("Invalid number of moves."); return;
        }

        List<String> userMoves = parseMoves(movesSeqRaw);
        List<String> canonical = computeCanonicalSolution(numDisks, pegChoice.getValue());
        boolean isCorrect = canonical.equals(userMoves);

        HanoiSolution solution = new HanoiSolution();
        solution.setPlayerName(player);
        solution.setNumDisks(numDisks);
        solution.setNumPegs(pegChoice.getValue());
        solution.setMovesSequence(String.join(",", userMoves));
        solution.setTimeTakenMs(0);
        solution.setAlgorithm("UserInput");

        TowerHanoiDAO.savePlayerSolution(solution, isCorrect, null);

        if (isCorrect) showAlert("✅ Moves submitted successfully and VERIFIED correct!");
        else showAlert("❌ Submitted moves did not match canonical solution. Try again or auto-solve to see answer.");

        moveSequenceInput.clear();
        movesField.clear();
    }

    // ------------------- AUTO-SOLVE -------------------
    private void autoSolve3Recursive() { runAlgorithmAndAnimate("3-Rec", () -> HanoiClassic3Pegs.solveRecursive(numDisks,'A','C','B'),3); }
    private void autoSolve3Iterative() { runAlgorithmAndAnimate("3-Iter", () -> HanoiClassic3Pegs.solveIterative(numDisks,'A','C','B'),3); }
    private void autoSolve4FrameStewart() { runAlgorithmAndAnimate("4-Frame-Stewart", () -> HanoiClassic4Pegs.solveFrameStewart(numDisks,'A','D','B','C'),4); }
    private void autoSolve4Naive() { runAlgorithmAndAnimate("4-Naive", () -> HanoiClassic4Pegs.solveNaive(numDisks,'A','D','B','C'),4); }

    private void runAlgorithmAndAnimate(String name, SolverRunnable solver, int pegCount) {
        if (numDisks <= 0) { showAlert("Start a game first."); return; }
        if (pegChoice.getValue() != pegCount) { showAlert("Change peg choice to " + pegCount + " and start game."); return; }

        try {
            long t0 = System.nanoTime();
            List<String> moves = solver.run();
            long t1 = System.nanoTime();
            long elapsedMs = (t1-t0)/1_000_000L;

            moveLog.appendText("[" + name + "] solved in " + elapsedMs + "ms\n");
            moveSequenceInput.setText(String.join(",", moves));
            movesField.setText(String.valueOf(moves.size()));

            HanoiSolution sol = new HanoiSolution();
            sol.setPlayerName(playerNameInput.getText().trim().isEmpty()?"Algorithm":playerNameInput.getText().trim());
            sol.setNumDisks(numDisks);
            sol.setNumPegs(pegCount);
            sol.setMovesSequence(String.join(",", moves));
            sol.setTimeTakenMs(elapsedMs);
            sol.setAlgorithm(name);
            TowerHanoiDAO.savePlayerSolution(sol, true, elapsedMs);

            // Animate each move sequentially
            animateSolutionMoves(moves);

        } catch (Exception ex) { ex.printStackTrace(); showAlert("Failed to run algorithm: " + ex.getMessage()); }
    }

    private void animateSolutionMoves(List<String> moves) {
        SequentialTransition seqAll = new SequentialTransition();

        for (String move : moves) {
            // Parse move text like "Move disk ? from A to C"
            String[] parts = move.split("from|to");
            if (parts.length < 3) continue;
            int from = parts[1].trim().charAt(0) - 'A';
            int to = parts[2].trim().charAt(0) - 'A';

            Rectangle disk = pegStacks.get(from).peek();
            if (disk == null) continue;

            VBox sourceVBox = pegVBoxes.get(from);
            VBox targetVBox = pegVBoxes.get(to);

            pegStacks.get(from).pop();
            sourceVBox.getChildren().remove(disk);

            double up = -50;
            double side = (to - from) * 50;
            double down = targetVBox.getChildren().size() * 30;

            TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), disk);
            moveUp.setByY(up);

            TranslateTransition moveSide = new TranslateTransition(Duration.millis(300), disk);
            moveSide.setByX(side);

            TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), disk);
            moveDown.setByY(-up + down);

            SequentialTransition diskSeq = new SequentialTransition(moveUp, moveSide, moveDown);
            diskSeq.setOnFinished(e -> {
                pegStacks.get(to).push(disk);
                targetVBox.getChildren().add(disk);
                disk.setTranslateX(0);
                disk.setTranslateY(0);
                movesCount++;
                moveLog.appendText("Moved disk to Peg " + (to+1) + "\n");
                checkWin();
            });

            seqAll.getChildren().add(diskSeq);
        }

        seqAll.play();
    }

    private interface SolverRunnable { List<String> run(); }

    private List<String> computeCanonicalSolution(int n, int pegs) {
        if (pegs == 3) return HanoiClassic3Pegs.solveRecursive(n,'A','C','B');
        else return HanoiClassic4Pegs.solveFrameStewart(n,'A','D','B','C');
    }

    private List<String> parseMoves(String raw) {
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();
        String[] parts = raw.split("[,;\\n]+");
        List<String> parsed = new ArrayList<>();
        for (String p : parts) {
            String s = p.trim();
            if (s.isEmpty()) continue;
            String normalized = s.replaceAll("\\s+", "").replace("->","-").replace("to","-");
            String[] arrowParts = normalized.split("-");
            if (arrowParts.length>=2) {
                String from = arrowParts[0].replaceAll("[^A-Za-z]","");
                String to = arrowParts[1].replaceAll("[^A-Za-z]","");
                parsed.add("Move disk ? from "+from.toUpperCase()+" to "+to.toUpperCase());
            } else parsed.add(s);
        }
        return parsed.stream().map(String::trim).collect(Collectors.toList());
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-padding: 8px; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white;";
    }

    private int parseDiskInputOrFallback(int fallback) {
        try { return Integer.parseInt(diskInput.getText().trim()); } catch (Exception e) { return fallback; }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Tower of Hanoi");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private Button modernBtn(String text, String c1, String c2) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI",14));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(180);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-radius:20;-fx-background-color:linear-gradient(to right,"+c1+","+c2+");");
        DropShadow shadow = new DropShadow(8, Color.rgb(0,0,0,0.35));
        btn.setEffect(shadow);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius:20;-fx-background-color:linear-gradient(to right,"+c2+","+c1+");"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius:20;-fx-background-color:linear-gradient(to right,"+c1+","+c2+");"));
        return btn;
    }

    public static void open(Stage stage) { new HanoiUI(stage); }
}
