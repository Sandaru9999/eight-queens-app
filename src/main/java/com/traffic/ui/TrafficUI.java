package com.traffic.ui;

import com.traffic.db.TrafficDAO;
import com.traffic.logic.MaxFlowDinic;
import com.traffic.logic.MaxFlowEdmondsKarp;
import com.traffic.models.TrafficSolution;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

public class TrafficUI {

    private Stage stage;
    private VBox root;
    private TextField playerNameField, playerFlowField;
    private TextArea output;
    private int[][] capacity = new int[9][9];
    private int correctFlow;

    public TrafficUI(Stage stage) {
        this.stage = stage;
        root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Traffic Simulation Game");
        title.setFont(Font.font("Arial", 28));
        title.setTextFill(Color.DARKGREEN);

        playerNameField = new TextField();
        playerNameField.setPromptText("Enter your name");
        playerNameField.setMaxWidth(250);

        playerFlowField = new TextField();
        playerFlowField.setPromptText("Enter your answer (Max Flow)");

        output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(250);

        Button generateGraphBtn = new Button("Generate Random Graph");
        generateGraphBtn.setOnAction(e -> generateGraph());

        Button submitBtn = new Button("Submit Answer");
        submitBtn.setOnAction(e -> checkAnswer());

        Button backBtn = new Button("Back to Menu");
        backBtn.setOnAction(e -> com.eightqueens.ui.MenuScreen.open(stage));

        root.getChildren().addAll(title, playerNameField, playerFlowField, generateGraphBtn, submitBtn, output, backBtn);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Traffic Simulation");
        stage.show();
    }

    private void generateGraph() {
        Random rand = new Random();
        capacity = new int[9][9];

        capacity[0][1] = 5 + rand.nextInt(11); // A->B
        capacity[0][2] = 5 + rand.nextInt(11); // A->C
        capacity[0][3] = 5 + rand.nextInt(11); // A->D
        capacity[1][4] = 5 + rand.nextInt(11); // B->E
        capacity[1][5] = 5 + rand.nextInt(11); // B->F
        capacity[2][4] = 5 + rand.nextInt(11); // C->E
        capacity[2][5] = 5 + rand.nextInt(11); // C->F
        capacity[3][5] = 5 + rand.nextInt(11); // D->F
        capacity[4][6] = 5 + rand.nextInt(11); // E->G
        capacity[4][7] = 5 + rand.nextInt(11); // E->H
        capacity[5][7] = 5 + rand.nextInt(11); // F->H
        capacity[6][8] = 5 + rand.nextInt(11); // G->T
        capacity[7][8] = 5 + rand.nextInt(11); // H->T

        MaxFlowEdmondsKarp ek = new MaxFlowEdmondsKarp(copyMatrix(capacity));
        MaxFlowDinic dinic = new MaxFlowDinic(copyMatrix(capacity));
        correctFlow = Math.max(ek.maxFlow(0,8), dinic.maxFlow(0,8));

        output.setText("Random Traffic Graph generated!\nTry to guess the max flow from A->T.");
    }

    private int[][] copyMatrix(int[][] mat) {
        int[][] copy = new int[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) System.arraycopy(mat[i], 0, copy[i], 0, mat[i].length);
        return copy;
    }

    private void checkAnswer() {
        String playerName = playerNameField.getText().trim();
        String answerText = playerFlowField.getText().trim();
        if (playerName.isEmpty() || answerText.isEmpty()) {
            output.appendText("\nEnter your name and answer!");
            return;
        }

        int playerAnswer;
        try { playerAnswer = Integer.parseInt(answerText); }
        catch (NumberFormatException e) { output.appendText("\nEnter a valid number!"); return; }

        String result = (playerAnswer == correctFlow) ? "Correct ✅" : "Wrong ❌, correct: " + correctFlow;
        output.appendText("\n" + result);

        TrafficSolution solution = new TrafficSolution();
        solution.setPlayerName(playerName);
        solution.setPlayerAnswer(playerAnswer);
        solution.setCorrectFlow(correctFlow);
        solution.setTimeTakenMs(0);

        TrafficDAO.savePlayerSolution(solution);
    }

    public static void open(Stage stage) {
        new TrafficUI(stage);
    }
}
