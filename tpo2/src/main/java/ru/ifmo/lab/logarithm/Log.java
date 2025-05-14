package ru.ifmo.lab.logarithm;

import ru.ifmo.lab.functions.ExpandableFunction;

import java.math.BigDecimal;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

public class Log extends ExpandableFunction {
    private final Ln ln;
    private final int base;

    public Log(final int base) {
        super();
        this.ln = new Ln();
        this.base = base;
        validateBase();
    }

    public Log(final Ln ln, final int base) {
        super();
        this.ln = ln;
        this.base = base;
        validateBase();
    }

    private void validateBase() {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException(
                format("Основание логарифма должно быть положительным и не равным 1. Получено: %d", base)
            );
        }
    }

    @Override
    public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
            throws ArithmeticException {
        checkValidity(x, precision);

        if (x.compareTo(ZERO) <= 0) {
            throw new ArithmeticException(
                format("Значение функции log_%d(%s) не существует: аргумент должен быть положительным", base, x)
            );
        }

        if (x.compareTo(ONE) == 0) {
            return ZERO.setScale(precision.scale(), HALF_EVEN); // log_b(1) = 0 для любого b
        }

        final BigDecimal lnX = ln.calculate(x, precision);
        final BigDecimal lnBase = ln.calculate(new BigDecimal(base), precision);

        if (lnBase.compareTo(ZERO) == 0) {
            throw new ArithmeticException("Деление на ноль: ln(base) = 0");
        }

        return lnX.divide(lnBase, DECIMAL128.getPrecision(), HALF_EVEN)
                .setScale(precision.scale(), HALF_EVEN);
    }
}