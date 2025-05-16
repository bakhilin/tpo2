import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        cos.calculate(new BigDecimal(1), new BigDecimal("0.0001"));

        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    void testCalculateWithMockSin() {
        final BigDecimal arg = new BigDecimal(2);
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
        final BigDecimal expected = new BigDecimal("0.4872");
        assertEquals(expected, cos.calculate(new BigDecimal(-200), DEFAULT_PRECISION));
    }

    @Test
    void testCalculateWithInvalidPrecision() {
        Cos cos = new Cos();
        assertThrows(ArithmeticException.class, 
            () -> cos.calculate(ONE, new BigDecimal("-0.0001")));
        assertThrows(ArithmeticException.class,
            () -> cos.calculate(ONE, ZERO));
    }


    @ParameterizedTest
    @CsvSource({
        "0.0, 1.0",
        "3.141592653589793, -1.0",
        "6.283185307179586, 1.0",
        "1.0471975511965976, 0.5",  
        "2.0943951023931953, -0.5"  
    })
    void testCalculateForSpecialValues(double x, double expected) {
        BigDecimal arg = new BigDecimal(x);
        BigDecimal expectedResult = new BigDecimal(expected);
        BigDecimal actual = new Cos().calculate(arg, DEFAULT_PRECISION);
        
        assertEquals(expectedResult.doubleValue(), actual.doubleValue(), 0.0001);
    }
}