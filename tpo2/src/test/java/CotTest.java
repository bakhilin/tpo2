import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;

import static java.math.MathContext.DECIMAL128;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.*;
import ch.obermuhlner.math.big.BigDecimalMath;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Cot;
import ru.ifmo.lab.trigonometry.Sin;

@ExtendWith(MockitoExtension.class)
class CotTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

    @Mock private Sin mockSin;
    @Mock private Cos mockCos;
    @Spy private Sin spySin;

    @Test
    void testCallSinAndCosFunctions() {
        final Cos cos = new Cos(spySin);
        final Cos spyCos = spy(cos);

        final Cot cot = new Cot(spySin, spyCos);
        cot.calculate(ONE, DEFAULT_PRECISION);

        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
        verify(spyCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void testCalculateWithMockSinAndMockCos() {
        final BigDecimal arg = new BigDecimal(5);

        when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("-0.95892427"));
        when(mockCos.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("0.28366218"));

        final Cot cot = new Cot(mockSin, mockCos);
        final BigDecimal expectedResult = new BigDecimal("-0.2958");
        assertEquals(expectedResult, cot.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateWithMockSin() {
        final BigDecimal arg = new BigDecimal(5);

        when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("-0.95892427"));

        final Cot cot = new Cot(mockSin, new Cos());
        final BigDecimal expectedResult = new BigDecimal("-0.2959");
        assertEquals(expectedResult, cot.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateWithMockCos() {
        final BigDecimal arg = new BigDecimal(5);

        when(mockCos.calculate(eq(arg), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("0.28366218"));

        final Cot cot = new Cot(new Sin(), mockCos);
        final BigDecimal expectedResult = new BigDecimal("-0.2958");
        assertEquals(expectedResult, cot.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testNotCalculateForZero() {
        final Cot cot = new Cot();
        assertThrows(ArithmeticException.class, () -> cot.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPiDividedByTwo() {
        final Cot cot = new Cot();
        final MathContext mc = new MathContext(DECIMAL128.getPrecision());
        final BigDecimal arg =
                BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
        assertEquals(ZERO.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), cot.calculate(arg, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForOne() {
        final Cot cot = new Cot();
        final BigDecimal expected = new BigDecimal("0.6421");
        assertEquals(expected, cot.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPeriodic() {
        final Cot cot = new Cot();
        final BigDecimal expected = new BigDecimal("-0.5235");
        assertEquals(expected, cot.calculate(new BigDecimal(134), DEFAULT_PRECISION));
    }
}