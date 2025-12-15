package com.eightqueens.ui;

import com.eightqueens.logic.EightQueensSequential;
import com.eightqueens.logic.EightQueensThreaded;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class AlgorithmPerformanceChart {

    public static void showChart() {

        Stage stage = new Stage();
        stage.setTitle("Eight Queens – Algorithm Performance (15 Rounds)");

        NumberAxis xAxis = new NumberAxis(1, 15, 1);
        xAxis.setLabel("Game Round");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time Taken (ms)");

        LineChart<Number, Number> chart =
                new LineChart<>(xAxis, yAxis);
        chart.setTitle("Sequential vs Threaded Performance");

        XYChart.Series<Number, Number> sequentialSeries =
                new XYChart.Series<>();
        sequentialSeries.setName("Sequential Algorithm");

        XYChart.Series<Number, Number> threadedSeries =
                new XYChart.Series<>();
        threadedSeries.setName("Threaded Algorithm");

        // ---- RUN 15 GAME ROUNDS ----
        for (int round = 1; round <= 15; round++) {

            long seqStart = System.currentTimeMillis();
            EightQueensSequential.solve();
            long seqTime = System.currentTimeMillis() - seqStart;

            long thStart = System.currentTimeMillis();
            EightQueensThreaded.solve();
            long thTime = System.currentTimeMillis() - thStart;

            sequentialSeries.getData().add(
                    new XYChart.Data<>(round, seqTime));

            threadedSeries.getData().add(
                    new XYChart.Data<>(round, thTime));
        }

        chart.getData().addAll(sequentialSeries, threadedSeries);

        Scene scene = new Scene(chart, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
}
