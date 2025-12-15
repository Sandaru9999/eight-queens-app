package com.traffic;

import com.eightqueens.ui.MenuScreen; 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.logging.Logger;


public class App extends Application { 

    private GameService gameService = new GameService();
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

   
    private BorderPane root; 
    private Pane graphPane;
    private Label resultLabel;
    private TextField flowInput;
    private Label playerNameLabel;

    @Override
    public void start(Stage stage) {
        
        this.root = new BorderPane();
        root.setPadding(new javafx.geometry.Insets(10));
        root.setStyle("-fx-background-color: #F4F4F9;");

      
        this.playerNameLabel = new Label("Current Player: Guest");
        this.playerNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12pt; -fx-text-fill: #333366;");

        Button changePlayerButton = new Button("Change Player");
        changePlayerButton.setStyle("-fx-background-color: #A9A9A9; -fx-text-fill: white; -fx-font-weight: bold;");
        changePlayerButton.setPrefWidth(200);

        Label titleLabel = new Label("🚗 Traffic Flow Game");
        titleLabel.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        Label promptLabel = new Label("Enter Max Flow (vehicles/min):");
        this.flowInput = new TextField();
        this.flowInput.setPromptText("Your guess...");
        this.flowInput.setMaxWidth(200);

        Button submitButton = new Button("Submit Guess");
        submitButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; -fx-font-weight: bold;");
        submitButton.setPrefWidth(200);

        Button newGameButton = new Button("Start New Round");
        newGameButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-weight: bold;");
        newGameButton.setPrefWidth(200);

        Button backToMenuButton = new Button("← Back to Menu");
        backToMenuButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold;");
        backToMenuButton.setPrefWidth(200);


        this.resultLabel = new Label("Ready to start! Capacities are randomized.");
        this.resultLabel.setWrapText(true);

      
        VBox controlPanel = new VBox(15, playerNameLabel, changePlayerButton, new Separator(), titleLabel, promptLabel, flowInput, submitButton, newGameButton, new Separator(), resultLabel, new Separator(), backToMenuButton);
        
        controlPanel.setPadding(new javafx.geometry.Insets(10));
        controlPanel.setMaxWidth(300);
        root.setLeft(controlPanel);

      
        this.graphPane = new GraphPane(gameService.getCurrentNetwork().getCapacity()); // Assumes GraphPane exists
        root.setCenter(graphPane);

       
        submitButton.setOnAction(e -> handleSubmitGuess());
        newGameButton.setOnAction(e -> handleNewGame());
        changePlayerButton.setOnAction(e -> promptForName(false));

    
        backToMenuButton.setOnAction(e -> handleBackToMenu(stage));


        
        Scene scene = new Scene(root, 1050, 600);
        stage.setTitle("Traffic Max Flow Game");
        stage.setScene(scene);
        stage.show();

       
        promptForName(true);
    }
    
    
    private void handleBackToMenu(Stage stage) {
       
        MenuScreen.open(stage);
    }
    
   
    public static void open(Stage stage) {
       
        App appInstance = new App(); 
       
        appInstance.start(stage);
    }
    

    private void updateGraphDisplay() {
        
        this.graphPane = new GraphPane(gameService.getCurrentNetwork().getCapacity()); 
        root.setCenter(this.graphPane);
    }


    private void handleSubmitGuess() {
       
        try {
            int playerGuess = Integer.parseInt(flowInput.getText().trim());

            GameService.GameResult result = gameService.findAndRecordMaxFlow();
            int correctFlow = result.getCorrectFlow();

            String outcome;
            if (playerGuess == correctFlow) {
                outcome = "🎉 WIN! You correctly identified the maximum flow!";
                promptForNameAndSave(result);
            } else if (Math.abs(playerGuess - correctFlow) <= 2) {
                outcome = "⚠️ DRAW! Close, but the correct flow was: " + correctFlow;
            } else {
                outcome = "❌ LOSE. The correct maximum flow was: " + correctFlow;
            }

            displayFinalResults(outcome, correctFlow, result.getTimeEK(), result.getTimeDinic());

        } catch (NumberFormatException ex) {
            resultLabel.setText("❌ Invalid Input. Please enter a whole number.");
        } catch (Exception ex) {
            resultLabel.setText("An unexpected error occurred: " + ex.getMessage());
            LOGGER.severe("Error during guess submission: " + ex.getMessage());
        }
    }

    private void handleNewGame() {
        gameService.startNewRound();
        updateGraphDisplay();
        flowInput.clear();
        resultLabel.setText("New game started! Find the max flow for the new network.");
    }

    private void displayFinalResults(String outcome, int correctFlow, double timeEK, double timeDinic) { 
        
      
        double timeDinicNanos = timeDinic * 1_000_000.0; 
        
        resultLabel.setText(String.format(
            "%s\n\n--- Algorithm Analysis ---\nCorrect Max Flow: %d vehicles/min\n" +
            "Edmonds-Karp Time: %.5f ms\n" +      
            "Dinic's Time: %.0f ns (Usually much faster)",
            outcome, correctFlow, timeEK, timeDinicNanos 
        ));
    }

    private void promptForName(boolean initial) {
        TextInputDialog dialog = new TextInputDialog(playerNameLabel.getText().replace("Current Player: ", ""));
        dialog.setTitle(initial ? "Welcome" : "Change Player Name");
        dialog.setHeaderText(initial ? "Please enter your name to begin." : "Enter new player name.");
        dialog.setContentText("Your Name:");

        Optional<String> nameResult = dialog.showAndWait();
        nameResult.ifPresent(playerName -> {
            String name = playerName.trim();
            if (name.length() > 0) {
                playerNameLabel.setText("Current Player: " + name);
            } else if (initial) {
                playerNameLabel.setText("Current Player: Guest");
            }
        });
    }

    private void promptForNameAndSave(GameService.GameResult result) {
        String currentName = playerNameLabel.getText().replace("Current Player: ", "");
        if ("Guest".equals(currentName) || currentName.isEmpty()) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Congratulations!");
            dialog.setHeaderText("You won! Please enter your name to save your record.");
            dialog.setContentText("Your Name:");

            Optional<String> nameResult = dialog.showAndWait();
            nameResult.ifPresent(playerName -> {
                String name = playerName.trim();
                if (name.length() > 0) {
                    playerNameLabel.setText("Current Player: " + name);
                    gameService.saveRecord(name, result);
                } else {
                    resultLabel.setText(resultLabel.getText() + "\n(Record not saved: Name was empty)");
                }
            });
        } else {
             gameService.saveRecord(currentName, result);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
