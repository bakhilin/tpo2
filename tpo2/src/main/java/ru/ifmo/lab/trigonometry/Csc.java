package ru.ifmo.lab.trigonometry;

import ru.ifmo.lab.functions.ExpandableFunction;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

public class Csc extends ExpandableFunction {
    private final Sin sin;

    public Csc(final Sin sin) {
        super();
        this.sin = sin;
    }

    public Csc() {
        super();
        this.sin = new Sin();
    }

    @Override
    public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
            throws ArithmeticException {
        checkValidity(x, precision);

        final BigDecimal sinValue = sin.calculate(x, precision);

        if (sinValue.compareTo(ZERO) == 0) {
            throw new ArithmeticException(format("Значение функции для аргумента %s не существует", x));
        }

        final BigDecimal result = BigDecimal.ONE.divide(sinValue, DECIMAL128.getPrecision(), HALF_EVEN);
        return result.setScale(precision.scale(), HALF_EVEN);
    }
}