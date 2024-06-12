package tntu.edu.ua.mygraphcalculator.functions;

import org.jfree.data.xy.XYSeries;

import java.util.function.Function;

public class FunctionFactory {

    public static XYSeries createTrigonometricFunction(String type, double A, double B, double C, double start, double end) {
        Function<Double, Double> func = switch (type.toLowerCase()) {
            case "sin" -> x -> A + B * Math.sin(C * x);
            case "cos" -> x -> A + B * Math.cos(C * x);
            case "tan" -> x -> A + B * Math.tan(C * x);
            case "ctan" -> x -> A + B / Math.tan(C * x);
            default -> throw new IllegalArgumentException("Unknown function type: " + type);
        };
        return createSeries(type + " function", func, start, end);
    }

    public static XYSeries createSecondOrderFunction(String type, double A, double B, double C, double start, double end) {
        Function<Double, Double> func;
        switch (type.toLowerCase()) {
            case "parabola":
                func = x -> A * x * x + B * x + C;
                break;
            case "hyperbola":
                func = x -> A / x;
                break;
            case "ellipse":
                return createParametricSeries("Ellipse", t -> A * Math.cos(t), t -> B * Math.sin(t), start, end);
            case "circle":
                return createParametricSeries("Circle", t -> A * Math.cos(t), t -> A * Math.sin(t), start, end);
            default:
                throw new IllegalArgumentException("Unknown function type: " + type);
        }
        return createSeries(type + " function", func, start, end);
    }

    public static XYSeries createExpLogFunction(String type, double A, double B, double start, double end) {
        Function<Double, Double> func = switch (type.toLowerCase()) {
            case "exp" -> x -> A * Math.exp(B * x);
            case "log" -> x -> A * Math.log(B * x);
            default -> throw new IllegalArgumentException("Unknown function type: " + type);
        };
        return createSeries(type + " function", func, start, end);
    }

    private static XYSeries createSeries(String name, Function<Double, Double> func, double start, double end) {
        XYSeries series = new XYSeries(name);
        for (double x = start; x <= end; x += 0.01) {
            try {
                series.add(x, func.apply(x));
            } catch (Exception e) {
                System.err.println("Error calculating function value at x=" + x + ": " + e.getMessage());
            }
        }
        return series;
    }

    private static XYSeries createParametricSeries(String name, Function<Double, Double> xFunc, Function<Double, Double> yFunc, double start, double end) {
        XYSeries series = new XYSeries(name);
        for (double t = start; t <= end; t += 0.01) {
            series.add(xFunc.apply(t), yFunc.apply(t));
        }
        return series;
    }
}