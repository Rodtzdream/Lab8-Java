package tntu.edu.ua.mygraphcalculator.functions;

import org.jfree.data.xy.XYSeries;
import java.util.function.Function;

/**
 * Клас FunctionFactory використовується для створення різних типів функцій, які можуть бути відображені на графіку.
 * Він включає в себе методи для створення тригонометричних функцій, функцій другого порядку та експоненціальних/логарифмічних функцій.
 */
public class FunctionFactory {

    /**
     * Створює тригонометричну функцію на основі заданих параметрів.
     *
     * @param type Тип тригонометричної функції (sin, cos, tan, ctan).
     * @param A, B, C Параметри функції.
     * @param start, end Діапазон значень x для яких буде обчислено значення функції.
     * @return Серія даних, яка представляє функцію.
     */
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

    /**
     * Створює функцію другого порядку на основі заданих параметрів.
     *
     * @param type Тип функції другого порядку (parabola, hyperbola, ellipse, circle).
     * @param A, B, C Параметри функції.
     * @param start, end Діапазон значень x для яких буде обчислено значення функції.
     * @return Серія даних, яка представляє функцію.
     */
    public static XYSeries createSecondOrderFunction(String type, double A, double B, double C, double start, double end) {
        Function<Double, Double> func;
        switch (type.toLowerCase()) {
            case "parabola":
                func = x -> A * x * x + B * x + C;
                break;
            case "hyperbola":
                func = x -> B * Math.sqrt((x * x / (A * A)) - 1);
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

    /**
     * Створює експоненціальну або логарифмічну функцію на основі заданих параметрів.
     *
     * @param type Тип функції (exp, log).
     * @param A, B Параметри функції.
     * @param start, end Діапазон значень x для яких буде обчислено значення функції.
     * @return Серія даних, яка представляє функцію.
     */
    public static XYSeries createExpLogFunction(String type, double A, double B, double start, double end) {
        Function<Double, Double> func = switch (type.toLowerCase()) {
            case "exp" -> x -> A * Math.exp(B * x);
            case "log" -> x -> A * Math.log(B * x);
            default -> throw new IllegalArgumentException("Unknown function type: " + type);
        };
        return createSeries(type + " function", func, start, end);
    }

    /**
     * Створює серію даних на основі заданої функції та діапазону значень.
     *
     * @param name Назва серії даних.
     * @param func Функція, яка визначає значення y для кожного x.
     * @param start Початкове значення діапазону x.
     * @param end Кінцеве значення діапазону x.
     * @return Серія даних, яка представляє функцію.
     */
    private static XYSeries createSeries(String name, Function<Double, Double> func, double start, double end) {
        XYSeries series = new XYSeries(name);
        for (double x = start; x <= end; x += 0.01) {
            try {
                double y = func.apply(x);
                series.add(x, y);
            } catch (Exception e) {
                System.err.println("Error calculating function value at x=" + x + ": " + e.getMessage());
            }
        }
        return series;
    }

    /**
     * Створює параметричну серію даних на основі заданих функцій та діапазону значень.
     *
     * @param name Назва серії даних.
     * @param xFunc Функція, яка визначає значення x для кожного параметра t.
     * @param yFunc Функція, яка визначає значення y для кожного параметра t.
     * @param start Початкове значення діапазону t.
     * @param end Кінцеве значення діапазону t.
     * @return Серія даних, яка представляє функцію.
     */
    private static XYSeries createParametricSeries(String name, Function<Double, Double> xFunc, Function<Double, Double> yFunc, double start, double end) {
        XYSeries series = new XYSeries(name);
        for (double t = start; t <= end; t += 0.01) {
            series.add(xFunc.apply(t), yFunc.apply(t));
        }
        return series;
    }
}