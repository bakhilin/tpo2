import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.math.MathContext.DECIMAL128;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.*;
import ch.obermuhlner.math.big.BigDecimalMath;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Sec;

@ExtendWith(MockitoExtension.class)
class SecTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Mock private Cos mockCos;
    @Spy private Cos spyCos;

    @Test
    void testCallCosFunction() {
        final Sec sec = new Sec(spyCos);
        sec.calculate(ONE, DEFAULT_PRECISION);

        verify(spyCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void testCalculateWithMockCos() {
        final BigDecimal arg = new BigDecimal(5);

        when(mockCos.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("0.28366218"));

        final Sec sec = new Sec(mockCos);
        final BigDecimal expectedResult = new BigDecimal("3.5253");
        assertEquals(expectedResult, sec.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testNotCalculateForPiDividedByTwo() {
        final Sec sec = new Sec();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        assertThrows(ArithmeticException.class, () -> sec.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForZero() {
        final Sec sec = new Sec();
        final BigDecimal expected = new BigDecimal("1.0000");
        assertEquals(expected, sec.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForOne() {
        final Sec sec = new Sec();
        final BigDecimal expected = new BigDecimal("1.8508");
        assertEquals(expected, sec.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPeriodic() {
        final Sec sec = new Sec();
        final BigDecimal expected = new BigDecimal("-2.1561");
        assertEquals(expected, sec.calculate(new BigDecimal(134), DEFAULT_PRECISION));
    }
}