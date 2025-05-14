import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Sin;

@ExtendWith(MockitoExtension.class)
class CosTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Mock private Sin mockSin;
    @Spy private Sin spySin;

    @Test
    void testCallSinFunction() {
        final Cos cos = new Cos(spySin);
        cos.calculate(new BigDecimal(6), new BigDecimal("0.001"));

        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void testCalculateWithMockSin() {
        final BigDecimal arg = new BigDecimal(5);
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal correctedArg =
                BigDecimalMath.pi(mc)
                        .divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN)
                        .subtract(arg);
        final BigDecimal sinResult = new BigDecimal("0.283662");

        when(mockSin.calculate(eq(correctedArg), any(BigDecimal.class))).thenReturn(sinResult);

        final Cos cos = new Cos(mockSin);

        assertEquals(sinResult, cos.calculate(arg, new BigDecimal("0.000001")));
    }

    @Test
    void testCalculateForZero() {
        final Cos cos = new Cos();
        assertEquals(ONE, cos.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPiDividedByTwo() {
        final Cos cos = new Cos();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        assertEquals(
                ZERO.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), cos.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForOne() {
        final Cos cos = new Cos();
        final BigDecimal expected = new BigDecimal("0.5403");
        assertEquals(expected, cos.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPeriodic() {
        final Cos cos = new Cos();
        final BigDecimal expected = new BigDecimal("-0.8797");
        assertEquals(expected, cos.calculate(new BigDecimal(-543), DEFAULT_PRECISION));
    }
}