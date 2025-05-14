package ru.ifmo.lab;

import ru.ifmo.lab.functions.BasicFunction;

import java.io.IOException;
import java.math.BigDecimal;



public class Csv {
    public static void write(
            String filename,
            BasicFunction function,
            BigDecimal from,
            BigDecimal to,
            BigDecimal step,
            BigDecimal precision) throws IOException {
        try (java.io.FileWriter writer = new java.io.FileWriter(filename)) {
            writer.write("x,f(x)\n");

            for (BigDecimal x = from; x.compareTo(to) <= 0; x = x.add(step)) {
                try {
                    BigDecimal result = function.calculate(x, precision);
                    writer.write(x + "," + result + "\n");
                } catch (ArithmeticException e) {
                    System.err.println("Пропущено значение x = " + x + ": " + e.getMessage());
                }
            }
        }
    }

}