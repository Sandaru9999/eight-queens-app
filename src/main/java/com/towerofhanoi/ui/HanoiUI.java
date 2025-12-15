package com.towerofhanoi.ui;

import com.eightqueens.ui.MenuScreen;
import com.towerofhanoi.db.TowerHanoiDAO;
import com.towerofhanoi.logic.HanoiClassic3Pegs;
import com.towerofhanoi.logic.HanoiClassic4Pegs;
import com.towerofhanoi.models.HanoiSolution;
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
    private ChoiceBox<Integer> pegChoice;
    private ChoiceBox<String> algoChoice;
    private TextField playerNameInput;
    private HBox pegBoxes;
    private final List<Stack<Rectangle>> pegStacks = new ArrayList<>();
    private final List<VBox> pegVBoxes = new ArrayList<>();
    private Rectangle selectedDisk;
    private int numDisks;
    private int movesCount = 0;
    private Label playerLabel;

    private TextField movesField;
    private TextArea moveSequenceInput;
    private Button submitMovesBtn;

    private TextArea moveLog;

    public HanoiUI(Stage stage) {
        this.stage = stage;

        root = new VBox(18);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #111111, #1a1a1a);");

        Label title = new Label("🗼 Tower of Hanoi");
        title.setFont(Font.font("Segoe UI", 36));
        title.setTextFill(Color.web("#00FFFF"));
        title.setEffect(new DropShadow(12, Color.BLACK));

        playerNameInput = new TextField();
        playerNameInput.setPromptText("Player name");
        playerNameInput.setMaxWidth(200);
        playerNameInput.setStyle(inputStyle());

        playerLabel = new Label("");
        playerLabel.setFont(Font.font(16));
        playerLabel.setTextFill(Color.LIGHTCYAN);

        pegChoice = new ChoiceBox<>();
        pegChoice.getItems().addAll(3, 4);
        pegChoice.setValue(3);
        pegChoice.setStyle("-fx-background-radius: 10; -fx-font-size: 14px;");
        pegChoice.setOnAction(e -> updateAlgoChoices());

        algoChoice = new ChoiceBox<>();
        updateAlgoChoices();

        Button startBtn = modernBtn("Start Game", "#00FFAA", "#00CC88");
        startBtn.setOnAction(e -> startGame());

        Button randomBtn = modernBtn("Random Setup", "#FFAA00", "#CC8800");
        randomBtn.setOnAction(e -> randomGame());

        Button backBtn = modernBtn("Back to Menu", "#FF5555", "#CC3333");
        backBtn.setOnAction(e -> MenuScreen.open(stage));

        HBox controls = new HBox(10, playerNameInput, pegChoice, algoChoice, startBtn, randomBtn, backBtn);
        controls.setAlignment(Pos.CENTER);

        pegBoxes = new HBox(50);
        pegBoxes.setAlignment(Pos.BOTTOM_CENTER);
        pegBoxes.setPrefHeight(340);

        movesField = new TextField();
        movesField.setPromptText("Number of Moves");
        movesField.setMaxWidth(140);

        moveSequenceInput = new TextArea();
        moveSequenceInput.setPromptText("Sequence of moves auto-filled here...");
        moveSequenceInput.setPrefHeight(80);

        submitMovesBtn = modernBtn("Submit Moves", "#00CCFF", "#0088CC");
        submitMovesBtn.setOnAction(e -> submitUserMoves());

        HBox moveControls = new HBox(10, movesField, submitMovesBtn);
        moveControls.setAlignment(Pos.CENTER);

        moveLog = new TextArea();
        moveLog.setPromptText("Move log...");
        moveLog.setPrefHeight(150);
        moveLog.setEditable(false);
        moveLog.setStyle("-fx-control-inner-background: #121212; -fx-text-fill: #00FFAA; -fx-font-family: 'Consolas'; -fx-font-size: 13px;");

        root.getChildren().addAll(title, playerLabel, controls, pegBoxes, moveSequenceInput, moveControls, moveLog);

        Scene scene = new Scene(root, 1300, 750);
        stage.setScene(scene);
        stage.setTitle("Tower of Hanoi - User Play");
        stage.show();
    }

    private void updateAlgoChoices() {
        algoChoice.getItems().clear();
        if (pegChoice.getValue() == 3) {
            algoChoice.getItems().addAll("Recursive", "Iterative");
            algoChoice.setValue("Recursive");
        } else {
            algoChoice.getItems().addAll("Frame-Stewart", "Naive");
            algoChoice.setValue("Frame-Stewart");
        }
    }

    private void startGame() {
        String player = playerNameInput.getText().trim();
        if (player.isEmpty()) { showAlert("Please enter player name."); return; }
        playerLabel.setText("👤 Player: " + player);

        Random rand = new Random();
        int disks = rand.nextInt(6) + 5; // 5–10 disks
        setupGame(disks, pegChoice.getValue());
        moveSequenceInput.clear();
        movesField.clear();
        moveLog.clear();
    }

    private void randomGame() {
        Random rand = new Random();
        int disks = rand.nextInt(6) + 5;
        int pegs = rand.nextInt(2) + 3;
        pegChoice.setValue(pegs);
        updateAlgoChoices();
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

            if (i == 0) {
                for (int d = disks; d >= 1; d--) {
                    double width = d * 20 + 40;
                    Rectangle disk = new Rectangle(width, 28, colors[(d - 1) % colors.length]);
                    disk.setArcWidth(16);
                    disk.setArcHeight(16);
                    disk.setEffect(new DropShadow(6, Color.rgb(0,0,0,0.35)));
                    disk.setOnMouseClicked(ev -> {
                        ev.consume();
                        selectDisk(disk);
                    });
                    stack.push(disk);
                    pegVBox.getChildren().add(0,disk);
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
                if (stack.peek() != disk) { showAlert("Only the TOP disk can be selected!"); return; }
                if (selectedDisk != null) { deselectDisk(); }
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
                sourcePeg = i; break;
            }
        }
        if (sourcePeg == -1 || sourcePeg == targetPeg) { deselectDisk(); return; }

        Stack<Rectangle> targetStack = pegStacks.get(targetPeg);
        if (!targetStack.isEmpty() && selectedDisk.getWidth() > targetStack.peek().getWidth()) {
            showAlert("Cannot place larger disk on smaller disk!"); deselectDisk(); return;
        }

        VBox sourceVBox = pegVBoxes.get(sourcePeg);
        VBox targetVBox = pegVBoxes.get(targetPeg);

        pegStacks.get(sourcePeg).pop();
        sourceVBox.getChildren().remove(selectedDisk);
        pegStacks.get(targetPeg).push(selectedDisk);
        targetVBox.getChildren().add(0, selectedDisk);

        animateDisk(selectedDisk);

        // Auto-fill move sequence
        char fromPeg = (char)('A' + sourcePeg);
        char toPeg = (char)('A' + targetPeg);
        moveSequenceInput.appendText(fromPeg + "->" + toPeg + ", ");

        deselectDisk();
        movesCount++;
        moveLog.appendText("Moved disk to Peg " + (targetPeg + 1) + "\n");
        checkWin();
    }

    private void deselectDisk() {
        if (selectedDisk != null) { selectedDisk.setStroke(null); selectedDisk.setStrokeWidth(0); selectedDisk = null; }
    }

    private void animateDisk(Rectangle disk) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(250), disk);
        tt.setFromY(-20); tt.setToY(0); tt.play();
    }

    private void checkWin() {
        int destIndex = pegChoice.getValue() == 3 ? 2 : 3;
        Stack<Rectangle> destPeg = pegStacks.get(destIndex);
        if (destPeg.size() == numDisks) {
            String player = playerNameInput.getText().trim();
            if (player.isEmpty()) player = "Player";
            showAlert("🏆 Congratulations, " + player + "!\nSolved in " + movesCount + " moves!");
        }
    }

    // ✅ Option B: Check moves legality instead of exact canonical sequence
    private void submitUserMoves() {
        String movesSeqRaw = moveSequenceInput.getText().trim();
        String movesCountText = movesField.getText().trim();
        String player = playerNameInput.getText().trim();

        if (player.isEmpty()) { showAlert("Please enter player name."); return; }
        if (movesSeqRaw.isEmpty()) { showAlert("No moves submitted."); return; }
        if (movesCountText.isEmpty()) { showAlert("Please enter the number of moves."); return; }

        int movesNumber;
        try { movesNumber = Integer.parseInt(movesCountText); } 
        catch (NumberFormatException e) { showAlert("Invalid number of moves."); return; }

        List<String> userMoves = parseMoves(movesSeqRaw);

        boolean isLegal = checkMovesLegal(userMoves, numDisks, pegChoice.getValue());

        // Save to DB
        HanoiSolution solution = new HanoiSolution();
        solution.setPlayerName(player);
        solution.setNumDisks(numDisks);
        solution.setNumPegs(pegChoice.getValue());
        solution.setMovesSequence(String.join(",", userMoves));
        solution.setTimeTakenMs(0);
        solution.setAlgorithm(algoChoice.getValue());
        TowerHanoiDAO.savePlayerSolution(solution, isLegal, 0L);

        if (isLegal) showAlert("✅ Moves submitted successfully! Solution is LEGAL.");
        else showAlert("❌ Submitted moves are ILLEGAL. Please follow Tower of Hanoi rules.");

        moveSequenceInput.clear();
        movesField.clear();
    }

    // ✅ Helper: Check Tower of Hanoi move legality
    private boolean checkMovesLegal(List<String> moves, int disks, int pegs) {
        List<Stack<Integer>> pegStacks = new ArrayList<>();
        for (int i = 0; i < pegs; i++) pegStacks.add(new Stack<>());

        for (int d = disks; d >= 1; d--) pegStacks.get(0).push(d);

        for (String move : moves) {
            String[] parts = move.split("->");
            if (parts.length != 2) return false;

            int from = parts[0].toUpperCase().charAt(0) - 'A';
            int to = parts[1].toUpperCase().charAt(0) - 'A';

            if (from < 0 || from >= pegs || to < 0 || to >= pegs) return false;

            Stack<Integer> source = pegStacks.get(from);
            Stack<Integer> target = pegStacks.get(to);

            if (source.isEmpty()) return false;
            int disk = source.pop();
            if (!target.isEmpty() && target.peek() < disk) return false;
            target.push(disk);
        }
        return true;
    }

    private List<String> parseMoves(String raw) {
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();
        String[] parts = raw.split("[,;\\n]+");
        List<String> parsed = new ArrayList<>();
        for (String p : parts) {
            String s = p.trim();
            if (s.isEmpty()) continue;
            String[] arrowParts = s.split("->");
            if (arrowParts.length >= 2) {
                String from = arrowParts[0].replaceAll("[^A-Za-z]","");
                String to = arrowParts[1].replaceAll("[^A-Za-z]","");
                parsed.add(from.toUpperCase() + "->" + to.toUpperCase());
            } else parsed.add(s);
        }
        return parsed;
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-padding: 8px; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white;";
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
