package com.gamemenu;

import com.eightqueens.ui.EightQueensScreen;
import com.towerofhanoi.ui.HanoiUI;
import com.tsp.ui.TSPUI;
import com.snakeandladder.ui.SnakeLadderUI;
import com.traffic.ui.TrafficUI;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class GameMenuController {

    private Stage stage;

    public GameMenuController(Stage stage) {
        this.stage = stage;
    }

    public void onEightQueensClick(ActionEvent event) {
        
        EightQueensScreen game = new EightQueensScreen(stage);
        stage.getScene().setRoot(game.getView());
    }

    public void onTowerOfHanoiClick(ActionEvent event) {
        
        HanoiUI.open(stage);
    }

    public void onGame3Click(ActionEvent event) {
        
        TSPUI.open(stage); 
    }

    public void onGame4Click(ActionEvent event) {
        new SnakeLadderUI(stage); 
    }

    public void onGame5Click(ActionEvent event) {
        
        TrafficUI.open(stage); 
    }
}