import java.math.BigDecimal;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import java.math.MathContext;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import ch.obermuhlner.math.big.BigDecimalMath;
import ru.ifmo.lab.trigonometry.Sin;

class SinTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Test
    void testCalculateForZero() {
        final Sin sin = new Sin();
        assertEquals(ZERO.setScale(4, HALF_EVEN), sin.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPiDividedByTwo() {
        final Sin sin = new Sin();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        assertEquals(
                ONE.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), sin.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForOne() {
        final Sin sin = new Sin();
        final BigDecimal expected = new BigDecimal("0.8415");
        assertEquals(expected, sin.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPeriodic() {
        final Sin sin = new Sin();
        final BigDecimal expected = new BigDecimal("0.0972");
        assertEquals(expected, sin.calculate(new BigDecimal(-113), DEFAULT_PRECISION));
    }
}