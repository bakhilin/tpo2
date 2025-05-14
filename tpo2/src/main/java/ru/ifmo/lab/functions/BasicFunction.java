package ru.ifmo.lab.functions;

import java.math.BigDecimal;

public interface BasicFunction {
    BigDecimal calculate(final BigDecimal x, final BigDecimal precision);
}
