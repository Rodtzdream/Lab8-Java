package tntu.edu.ua.mygraphcalculator.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import tntu.edu.ua.mygraphcalculator.functions.FunctionFactory;
import tntu.edu.ua.mygraphcalculator.functions.FunctionChecker;

/**
 * Клас FunctionPlotter використовується для створення графіків різних функцій.
 * Він включає в себе можливість додавання тригонометричних функцій, функцій другого порядку та експоненціальних/логарифмічних функцій.
 * Також він має можливість відображення точок перетину функцій.
 */
public class FunctionPlotter extends ApplicationFrame {

    /**
     * Колекція даних для графіків.
     */
    private final XYSeriesCollection dataset = new XYSeriesCollection();

    /**
     * Список серій даних для графіків.
     */
    private final List<XYSeries> seriesList = new ArrayList<>();

    /**
     * Мітка для відображення точок перетину.
     */
    private final JLabel intersectionLabel = new JLabel("Intersection: None");

    /**
     * Об'єкт для перевірки перетину функцій.
     */
    private final FunctionChecker functionChecker = new FunctionChecker();

    /**
     * Конструктор класу FunctionPlotter.
     *
     * @param title Назва вікна додатку.
     */
    public FunctionPlotter(String title) {
        super(title);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Графіки функцій",
                "X-axis",
                "Y-axis",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        // Додавання осей x та y
        ValueMarker xMarker = new ValueMarker(0);
        xMarker.setPaint(Color.BLACK);
        plot.addDomainMarker(xMarker);

        ValueMarker yMarker = new ValueMarker(0);
        yMarker.setPaint(Color.BLACK);
        plot.addRangeMarker(yMarker);

        // Налаштування осей з поділками
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setTickUnit(new NumberAxis().getTickUnit());
        domainAxis.setVisible(true);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberAxis().getTickUnit());
        rangeAxis.setVisible(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(750, 750));
        chartPanel.setMouseWheelEnabled(true); // Увімкнення масштабування колесом миші

        chartPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                double chartX = plot.getDomainAxis().java2DToValue(x, chartPanel.getScreenDataArea(), plot.getDomainAxisEdge());
                double chartY = plot.getRangeAxis().java2DToValue(y, chartPanel.getScreenDataArea(), plot.getRangeAxisEdge());
                functionChecker.checkIntersection(chartX, chartY, seriesList, intersectionLabel);

                double domainLength = plot.getDomainAxis().getRange().getLength();
                double rangeLength = plot.getRangeAxis().getRange().getLength();

                ((NumberAxis) plot.getDomainAxis()).setTickUnit(new NumberTickUnit(domainLength / 10));
                ((NumberAxis) plot.getRangeAxis()).setTickUnit(new NumberTickUnit(rangeLength / 10));
            }
        });
        setContentPane(chartPanel);

        JPanel controlPanel = new JPanel();
        JButton addTrigButton = new JButton("Add Trigonometric Function");
        addTrigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String function = JOptionPane.showInputDialog("Enter function type (sin, cos, tan, ctan):");
                double A = Double.parseDouble(JOptionPane.showInputDialog("Enter value for A:"));
                double B = Double.parseDouble(JOptionPane.showInputDialog("Enter value for B:"));
                double C = Double.parseDouble(JOptionPane.showInputDialog("Enter value for C:"));
                double start = Double.parseDouble(JOptionPane.showInputDialog("Enter start value of range:"));
                double end = Double.parseDouble(JOptionPane.showInputDialog("Enter end value of range:"));
                addFunction(FunctionFactory.createTrigonometricFunction(function, A, B, C, start, end));
            }
        });

        JButton addSecondOrderButton = new JButton("Add Second Order Curve");
        addSecondOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String function = JOptionPane.showInputDialog("Enter function type (parabola, hyperbola, ellipse, circle):");
                double A = Double.parseDouble(JOptionPane.showInputDialog("Enter value for A:"));
                double B = Double.parseDouble(JOptionPane.showInputDialog("Enter value for B:"));
                double C = Double.parseDouble(JOptionPane.showInputDialog("Enter value for C:"));
                double start = Double.parseDouble(JOptionPane.showInputDialog("Enter start value of range:"));
                double end = Double.parseDouble(JOptionPane.showInputDialog("Enter end value of range:"));
                addFunction(FunctionFactory.createSecondOrderFunction(function, A, B, C, start, end));
            }
        });

        JButton addExpLogButton = new JButton("Add Exp/Log Function");
        addExpLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String function = JOptionPane.showInputDialog("Enter function type (exp, log):");
                double A = Double.parseDouble(JOptionPane.showInputDialog("Enter value for A: (exp: multiplier, log: multiplier of logarithm"));
                double B = Double.parseDouble(JOptionPane.showInputDialog("Enter value for B: (exp: base, log: base of logarithm"));
                double start = Double.parseDouble(JOptionPane.showInputDialog("Enter start value of range:"));
                double end = Double.parseDouble(JOptionPane.showInputDialog("Enter end value of range:"));
                addFunction(FunctionFactory.createExpLogFunction(function, A, B, start, end));
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataset.removeAllSeries();
                seriesList.clear();
                intersectionLabel.setText("Intersection: None");
            }
        });

        controlPanel.add(addTrigButton);
        controlPanel.add(addSecondOrderButton);
        controlPanel.add(addExpLogButton);
        controlPanel.add(clearButton);
        controlPanel.add(intersectionLabel);
        add(controlPanel, BorderLayout.SOUTH);
    }

    /**
     * Метод для додавання нової функції до графіку.
     *
     * @param series Серія даних нової функції.
     */
    private void addFunction(XYSeries series) {
        series.setKey(series.getKey() + " " + seriesList.size());
        dataset.addSeries(series);
        seriesList.add(series);
    }

    /**
     * Головний метод для запуску додатку.
     *
     * @param args Аргументи командного рядка.
     */
    public static void main(String[] args) {
        FunctionPlotter example = new FunctionPlotter("Function Plotter");
        example.pack();
        RefineryUtilities.centerFrameOnScreen(example);
        example.setVisible(true);
    }
}
