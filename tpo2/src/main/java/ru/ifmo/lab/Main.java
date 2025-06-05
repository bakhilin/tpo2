package ru.ifmo.lab;

import java.io.IOException;
import java.math.BigDecimal;

import ru.ifmo.lab.functions.FinalFunction;
import ru.ifmo.lab.logarithm.Ln;
import ru.ifmo.lab.logarithm.Log;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Cot;
import ru.ifmo.lab.trigonometry.Csc;
import ru.ifmo.lab.trigonometry.Sec;
import ru.ifmo.lab.trigonometry.Sin;
import ru.ifmo.lab.trigonometry.Tan;

public class Main {

    public static void main(String[] ignoredArgs) throws IOException {
        final Cos cos = new Cos();
        Csv.write(
                "csv/cos.csv",
                cos,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Sin sin = new Sin();
        Csv.write(
                "csv/sin.csv",
                sin,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Tan tan = new Tan();
        Csv.write(
                "csv/tan.csv",
                tan,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Cot cot = new Cot();
        Csv.write(
                "csv/cot.csv",
                cot,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Csc csc = new Csc();
        Csv.write(
                "csv/csc.csv",
                csc,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Sec sec = new Sec();
        Csv.write(
                "csv/sec.csv",
                sec,
                new BigDecimal(-1),
                new BigDecimal(-1),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Ln ln = new Ln();
        Csv.write(
                "csv/ln.csv",
                ln,
                new BigDecimal(2),
                new BigDecimal(2),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Log log2 = new Log(2);
        Csv.write(
                "csv/log2.csv",
                log2,
                new BigDecimal(2),
                new BigDecimal(2),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Log log3 = new Log(3);
        Csv.write(
                "csv/log3.csv",
                log3,
                new BigDecimal(2),
                new BigDecimal(2),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final Log log5 = new Log(5);
        Csv.write(
                "csv/log5.csv",
                log5,
                new BigDecimal(2),
                new BigDecimal(2),
                new BigDecimal("0.1"),
                new BigDecimal("0.00000001"));

        final FinalFunction func = new FinalFunction();
        Csv.write(
                "csv/func.csv",
                func,
                new BigDecimal(-10),
                new BigDecimal(1),
                new BigDecimal("0.0001"),
                new BigDecimal("0.00000001"));
    }

}