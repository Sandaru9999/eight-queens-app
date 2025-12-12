package com.towerofhanoi.ui;

import com.eightqueens.ui.MenuScreen;
import com.towerofhanoi.db.TowerHanoiDAO;
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

    public HanoiUI(Stage stage) {
        this.stage = stage;

        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1724, #1e293b);");

        // TITLE
        Label title = new Label("🗼 Tower of Hanoi");
        title.setFont(Font.font("Arial", 36));
        title.setTextFill(Color.WHITE);
        title.setEffect(new DropShadow(12, Color.BLACK));

        // PLAYER NAME
        playerNameInput = new TextField();
        playerNameInput.setPromptText("Player name");
        playerNameInput.setMaxWidth(200);
        playerNameInput.setStyle(inputStyle());

        playerLabel = new Label("");
        playerLabel.setFont(Font.font(18));
        playerLabel.setTextFill(Color.LIGHTYELLOW);

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
        Button startBtn = createModernButton("Start Game");
        startBtn.setOnAction(e -> startGame());

        Button randomBtn = createModernButton("Random Setup");
        randomBtn.setOnAction(e -> randomGame());

        Button backBtn = createModernButton("Back to Menu");
        backBtn.setOnAction(e -> MenuScreen.open(stage));

        HBox controls = new HBox(12, playerNameInput, diskInput, pegChoice, startBtn, randomBtn, backBtn);
        controls.setAlignment(Pos.CENTER);

        // PEG BOXES
        pegBoxes = new HBox(40);
        pegBoxes.setAlignment(Pos.BOTTOM_CENTER);
        pegBoxes.setPrefHeight(360);

        // Move input section
        movesField = new TextField();
        movesField.setPromptText("Number of Moves");
        movesField.setMaxWidth(140);

        moveSequenceInput = new TextArea();
        moveSequenceInput.setPromptText("Enter sequence of moves (e.g., A->C,B->A)");
        moveSequenceInput.setPrefHeight(80);

        submitMovesBtn = createModernButton("Submit Moves");
        submitMovesBtn.setOnAction(e -> submitUserMoves());

        HBox moveControls = new HBox(10, movesField, submitMovesBtn);
        moveControls.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, playerLabel, controls, pegBoxes, moveSequenceInput, moveControls);

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Tower of Hanoi");
        stage.show();
    }

    private void startGame() {
        String player = playerNameInput.getText().trim();
        if (player.isEmpty()) {
            showAlert("Please enter player name.");
            return;
        }
        playerLabel.setText("👤 Player: " + player);

        int disks = parseDiskInputOrFallback(5);
        disks = Math.max(5, Math.min(10, disks));
        diskInput.setText(String.valueOf(disks));

        setupGame(disks, pegChoice.getValue());
        moveSequenceInput.clear();
        movesField.clear();
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

                    disk.setOnMouseClicked(ev -> {
                        ev.consume();
                        selectDisk(disk);
                    });

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
                selectedDisk.setStroke(Color.GOLD);
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
            selectedDisk.setStroke(null);
            selectedDisk.setStrokeWidth(0);
            selectedDisk = null;
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
        targetStack.push(selectedDisk);
        targetVBox.getChildren().add(selectedDisk);

        selectedDisk.setStroke(null);
        selectedDisk.setStrokeWidth(0);
        selectedDisk = null;
        movesCount++;
        checkWin();
    }

    private void checkWin() {
        Stack<Rectangle> lastPeg = pegStacks.get(pegStacks.size() - 1);
        if (lastPeg.size() == numDisks) {
            String player = playerNameInput.getText().trim();
            if (player.isEmpty()) player = "Player";
            showAlert("🏆 Congratulations, " + player + "!\nSolved in " + movesCount + " moves!");
        }
    }

    // ------------------- Submit Moves Functionality -------------------
    private void submitUserMoves() {
        String movesSeq = moveSequenceInput.getText().trim();
        String movesCountText = movesField.getText().trim();
        String player = playerNameInput.getText().trim();

        if (player.isEmpty()) {
            showAlert("Please enter player name.");
            return;
        }
        if (movesSeq.isEmpty()) {
            showAlert("Please enter the sequence of moves.");
            return;
        }
        if (movesCountText.isEmpty()) {
            showAlert("Please enter the number of moves.");
            return;
        }

        int movesNumber;
        try {
            movesNumber = Integer.parseInt(movesCountText);
        } catch (NumberFormatException e) {
            showAlert("Invalid number of moves.");
            return;
        }

        // Save to database
        HanoiSolution solution = new HanoiSolution();
        solution.setPlayerName(player);
        solution.setNumDisks(numDisks);
        solution.setNumPegs(pegChoice.getValue());
        solution.setMovesSequence(movesSeq);
        solution.setTimeTakenMs(0); // you can add timer if needed
        solution.setAlgorithm("UserInput");

        TowerHanoiDAO.savePlayerSolution(solution);
        showAlert("✅ Moves submitted successfully!");

        moveSequenceInput.clear();
        movesField.clear();
    }

    private int parseDiskInputOrFallback(int fallback) {
        try {
            return Integer.parseInt(diskInput.getText().trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-padding: 8px; -fx-font-size: 14px; -fx-background-color: rgba(255,255,255,0.85);";
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Tower of Hanoi");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private Button createModernButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font(15));
        btn.setTextFill(Color.WHITE);
        btn.setPrefWidth(140);
        btn.setPrefHeight(38);
        btn.setStyle("-fx-background-radius: 18; -fx-background-color: linear-gradient(to right, #FF416C, #FF4B2B);");
        DropShadow shadow = new DropShadow(8, Color.rgb(0, 0, 0, 0.35));
        btn.setEffect(shadow);

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-radius: 18; -fx-background-color: linear-gradient(to right, #FF4B2B, #FF416C);"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-radius: 18; -fx-background-color: linear-gradient(to right, #FF416C, #FF4B2B);"));
        return btn;
    }

    public static void open(Stage stage) {
        new HanoiUI(stage);
    }
}
