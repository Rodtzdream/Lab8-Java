package tntu.edu.ua.mygraphcalculator.functions;

import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.util.List;

public class FunctionChecker {

    public void checkIntersection(double chartX, double chartY, List<XYSeries> seriesList, JLabel intersectionLabel) {
        boolean intersectionFound = false;
        for (int i = 0; i < seriesList.size(); i++) {
            for (int j = i + 1; j < seriesList.size(); j++) {
                XYSeries series1 = seriesList.get(i);
                XYSeries series2 = seriesList.get(j);

                for (int k = 0; k < series1.getItemCount(); k++) {
                    double x1 = series1.getX(k).doubleValue();
                    double y1 = series1.getY(k).doubleValue();

                    for (int l = 0; l < series2.getItemCount(); l++) {
                        double x2 = series2.getX(l).doubleValue();
                        double y2 = series2.getY(l).doubleValue();

                        if (Math.abs(x1 - x2) < 0.01 && Math.abs(y1 - y2) < 0.01) {
                            double distance = Math.sqrt(Math.pow(chartX - x1, 2) + Math.pow(chartY - y1, 2));
                            if (distance < 0.05) {
                                intersectionLabel.setText(String.format("X: %.2f, Y: %.2f (Intersection: %.2f, %.2f)", chartX, chartY, x1, y1));
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (!intersectionFound) {
            intersectionLabel.setText(String.format("X: %.2f, Y: %.2f", chartX, chartY));
        }
    }
}
