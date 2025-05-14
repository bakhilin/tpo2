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
import ru.ifmo.lab.trigonometry.Csc;
import ru.ifmo.lab.trigonometry.Sin;

@ExtendWith(MockitoExtension.class)
class CscTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Mock private Sin mockSin;
    @Spy private Sin spySin;

    @Test
    void testCallSinFunction() {
        final Csc csc = new Csc(spySin);
        csc.calculate(ONE, DEFAULT_PRECISION);

        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void testCalculateWithMockSin() {
        final BigDecimal arg = new BigDecimal(5);

        when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("-0.95892427"));

        final Csc csc = new Csc(mockSin);
        final BigDecimal expectedResult = new BigDecimal("-1.0428");
        assertEquals(expectedResult, csc.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testNotCalculateForZero() {
        final Csc csc = new Csc();
        assertThrows(ArithmeticException.class, () -> csc.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPiDividedByTwo() {
        final Csc csc = new Csc();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        final BigDecimal expected = new BigDecimal("1.0000");
        assertEquals(expected, csc.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForOne() {
        final Csc csc = new Csc();
        final BigDecimal expected = new BigDecimal("1.1884");
        assertEquals(expected, csc.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPeriodic() {
        final Csc csc = new Csc();
        final BigDecimal expected = new BigDecimal("1.1288");
        assertEquals(expected, csc.calculate(new BigDecimal(134), DEFAULT_PRECISION));
    }
}