package com.tsp.ui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityMapPane extends Pane {

    private Map<Character, Circle> cityNodes = new HashMap<>();
    private int[][] distanceMatrix; // reference to distances

    public CityMapPane() {
        setPrefSize(500, 400);
        setStyle("-fx-background-color: rgba(255,255,255,0.35); -fx-background-radius: 20;");

        addCity('A', 80, 60);
        addCity('B', 200, 40);
        addCity('C', 350, 80);
        addCity('D', 100, 180);
        addCity('E', 260, 160);
        addCity('F', 420, 200);
        addCity('G', 70, 320);
        addCity('H', 220, 300);
        addCity('I', 360, 320);
        addCity('J', 460, 120);
    }

    private void addCity(char name, double x, double y) {
        Circle circle = new Circle(x, y, 14, Color.DODGERBLUE);
        circle.setStroke(Color.WHITE);
        Text label = new Text(x - 4, y - 20, String.valueOf(name));
        label.setFont(Font.font(14));
        getChildren().addAll(circle, label);
        cityNodes.put(name, circle);
    }

    public void setDistanceMatrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public void drawRoute(List<Character> route) {
        getChildren().removeIf(n -> n instanceof Line || n instanceof Text && ((Text)n).getUserData() != null);

        for (int i = 0; i < route.size() - 1; i++) {
            Circle c1 = cityNodes.get(route.get(i));
            Circle c2 = cityNodes.get(route.get(i + 1));

            if (c1 != null && c2 != null) {
                Line line = new Line(
                        c1.getCenterX(), c1.getCenterY(),
                        c2.getCenterX(), c2.getCenterY()
                );
                line.setStroke(Color.FORESTGREEN);
                line.setStrokeWidth(3);
                getChildren().add(line);

                // Add distance label at the midpoint of the line
                if (distanceMatrix != null) {
                    int idx1 = route.get(i) - 'A';
                    int idx2 = route.get(i + 1) - 'A';
                    int distance = distanceMatrix[idx1][idx2];

                    double midX = (c1.getCenterX() + c2.getCenterX()) / 2;
                    double midY = (c1.getCenterY() + c2.getCenterY()) / 2;

                    Text distLabel = new Text(midX, midY, String.valueOf(distance));
                    distLabel.setFont(Font.font(12));
                    distLabel.setFill(Color.RED);
                    distLabel.setUserData("distance"); // mark as distance label
                    getChildren().add(distLabel);
                }
            }
        }
    }
}
