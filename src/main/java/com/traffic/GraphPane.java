package com.traffic;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GraphPane extends Pane {
   
    private static final double[][] NODE_POSITIONS = {
        {50, 200},   
        {200, 100},  
        {200, 200}, 
        {200, 300},  
        {350, 150},  
        {350, 250}, 
        {500, 100},  
        {500, 300}, 
        {650, 200},  
        {0, 0}       
    };
    
    private static final double NODE_RADIUS = 15;

    public GraphPane(int[][] capacity) {
        this.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        drawGraph(capacity);
    }

    private void drawGraph(int[][] capacity) {
        this.getChildren().clear();
        int V = capacity.length;

       
        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                if (capacity[u][v] > 0) {
                    double startX = NODE_POSITIONS[u][0];
                    double startY = NODE_POSITIONS[u][1];
                    double endX = NODE_POSITIONS[v][0];
                    double endY = NODE_POSITIONS[v][1];

                    
                    Line line = new Line(startX, startY, endX, endY);
                    line.setStroke(Color.DARKGRAY);
                    line.setStrokeWidth(2);
                    this.getChildren().add(line);

                  
                    double textX = (startX + endX) / 2 + 5;
                    double textY = (startY + endY) / 2 - 5;
                    Text capText = new Text(textX, textY, "Cap: " + capacity[u][v]);
                    capText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                    capText.setFill(Color.BLUE);
                    this.getChildren().add(capText);
                }
            }
        }

        
        for (int i = 0; i < 9; i++) {
            double x = NODE_POSITIONS[i][0];
            double y = NODE_POSITIONS[i][1];

           
            Circle circle = new Circle(x, y, NODE_RADIUS, Color.LIGHTCORAL);
            circle.setStroke(Color.DARKRED);
            this.getChildren().add(circle);

           
            Text label = new Text(x - 5, y + 5, TrafficNetwork.NODE_NAMES[i]);
            label.setFill(Color.WHITE);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            this.getChildren().add(label);
        }
    }
}

