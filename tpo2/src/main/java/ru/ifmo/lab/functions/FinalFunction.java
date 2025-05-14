package ru.ifmo.lab.functions;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;
import java.math.MathContext;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import ru.ifmo.lab.logarithm.Ln;
import ru.ifmo.lab.logarithm.Log;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Cot;
import ru.ifmo.lab.trigonometry.Csc;
import ru.ifmo.lab.trigonometry.Sec;
import ru.ifmo.lab.trigonometry.Sin;
import ru.ifmo.lab.trigonometry.Tan;

public class FinalFunction implements BasicFunction {

    private final Sin sin;
    private final Tan tan;
    private final Cos cos;
    private final Cot cot;
    private final Csc csc;
    private final Sec sec;
    private final Ln ln;
    private final Log log2;
    private final Log log3;
    private final Log log5;

    public FinalFunction() {
        this.sin = new Sin();
        this.tan = new Tan();
        this.cos = new Cos();
        this.cot = new Cot();
        this.csc = new Csc();
        this.sec = new Sec();
        this.ln = new Ln();
        this.log2 = new Log(2);
        this.log3 = new Log(3);
        this.log5 = new Log(5);
    }

    public FinalFunction(Sin sin, Tan tan, Cos cos, Cot cot, Csc csc, Sec sec, Ln ln, Log log2, Log log3, Log log5) {
        this.sin = sin;
        this.tan = tan;
        this.cos = cos;
        this.cot = cot;
        this.csc = csc;
        this.sec = sec;
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
    }

    @Override
    public BigDecimal calculate(final BigDecimal x, final BigDecimal precision) {
        final MathContext mc = new MathContext(DECIMAL128.getPrecision(), HALF_EVEN);
        final BigDecimal correctedX = x.remainder(BigDecimalMath.pi(mc).multiply(new BigDecimal(2)));

        if (x.compareTo(ZERO) <= 0) {
            BigDecimal sinX = sin.calculate(correctedX, precision);
            BigDecimal cosX = cos.calculate(correctedX, precision);
            BigDecimal tanX = tan.calculate(correctedX, precision);
            BigDecimal cotX = cot.calculate(correctedX, precision);
            BigDecimal secX = sec.calculate(correctedX, precision);
            BigDecimal cscX = csc.calculate(correctedX, precision);

            BigDecimal numeratorPart1 = sinX.add(cosX).pow(3);
            BigDecimal numeratorPart2 = cosX.multiply(tanX);
            BigDecimal numeratorPart3 = numeratorPart1.add(numeratorPart2).divide(sinX, mc);
            BigDecimal numeratorPart4 = numeratorPart3.add(sinX).add(cosX);

           
            BigDecimal denominatorPart1 = tanX.multiply(cotX);
            BigDecimal denominatorPart2 = tanX.subtract(secX.multiply(cscX));
            BigDecimal denominatorPart3 = denominatorPart1.subtract(denominatorPart2).divide(secX, mc);

            BigDecimal denominatorPart4 = cosX.subtract(cotX).pow(2);
            BigDecimal denominatorPart5 = denominatorPart4.divide(tanX, mc).pow(3);

            BigDecimal denominator = denominatorPart3.divide(denominatorPart5, mc);

            return numeratorPart4.divide(denominator, mc).setScale(precision.scale(), HALF_EVEN);
        } else {
            BigDecimal log2X = log2.calculate(x, precision);
            BigDecimal lnX = ln.calculate(x, precision);
            BigDecimal log3X = log3.calculate(x, precision);
            BigDecimal log5X = log5.calculate(x, precision);

            BigDecimal part1 = log2X.subtract(lnX).pow(3);
            BigDecimal part2 = part1.divide(log2X.pow(3), mc);
            BigDecimal part3 = log3X.divide(log5X, mc);
            BigDecimal result = part2.add(part3).pow(2);

            return result.setScale(precision.scale(), HALF_EVEN);
        }
    }

    public Log getLog5() {
        return log5;
    }
}