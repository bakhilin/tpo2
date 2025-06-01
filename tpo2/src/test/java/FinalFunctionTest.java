import static java.math.BigDecimal.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.obermuhlner.math.big.BigDecimalMath;
import ru.ifmo.lab.functions.BasicFunction;
import ru.ifmo.lab.functions.FinalFunction;
import ru.ifmo.lab.logarithm.Ln;
import ru.ifmo.lab.logarithm.Log;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Cot;
import ru.ifmo.lab.trigonometry.Csc;
import ru.ifmo.lab.trigonometry.Sec;
import ru.ifmo.lab.trigonometry.Sin;
import ru.ifmo.lab.trigonometry.Tan;

@ExtendWith(MockitoExtension.class)
class FinalFunctionTest {
    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.00000001");
    private static final String PATH_TO_TEST_DATA = "/home/bakhilin/tpo2/tpo2/csv/";
    
    // Моки зависимостей
    @Mock private Sin mockSin;
    @Mock private Cos mockCos;
    @Mock private Tan mockTan;
    @Mock private Cot mockCot;
    @Mock private Csc mockCsc;
    @Mock private Sec mockSec;
    @Mock private Ln mockLn;
    @Mock private Log mockLog2;
    @Mock private Log mockLog3;
    @Mock private Log mockLog5;
    
    @Spy private Sin spySin;
    
    private static final Map<String, Map<BigDecimal, BigDecimal>> functionValues = new HashMap<>();

    @BeforeAll
    static void loadAllTestData() throws IOException {
        String[] functionFiles = {
            "sin.csv", "cos.csv", "tan.csv", 
            "cot.csv", "csc.csv", "sec.csv",
            "ln.csv", "log2.csv", "log3.csv", "log5.csv"
        };

        for (String file : functionFiles) {
            String functionName = file.replace(".csv", "");
            functionValues.put(functionName, loadFunctionValues(PATH_TO_TEST_DATA + file));
        }
    }

    private static Map<BigDecimal, BigDecimal> loadFunctionValues(String filePath) throws IOException {
        Map<BigDecimal, BigDecimal> values = new HashMap<>();
        Files.lines(Paths.get(filePath))
            .skip(1) // Skip header
            .forEach(line -> {
                String[] parts = line.split(",");
                BigDecimal x = new BigDecimal(parts[0].trim());
                BigDecimal value = new BigDecimal(parts[1].trim());
                values.put(x, value);
            });
        return values;
    }
    

    @Test
    void testCalculateWithDynamicMocks() {
        // Configure mocks to return values from CSV files
        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockSec, "sec");
        configureMock(mockLn, "ln");
        configureMock(mockLog2, "log2");
        configureMock(mockLog3, "log3");
        configureMock(mockLog5, "log5");

        BigDecimal x1 = new BigDecimal(-1);
        BigDecimal expected1 = new BigDecimal("-2.73560392");
        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, mockSec, mockLn, mockLog2, mockLog3, mockLog5);
        BigDecimal result1 = system.calculate(x1, DEFAULT_PRECISION);
        assertEquals(expected1, result1);

        BigDecimal x2 = new BigDecimal("2");
        BigDecimal expected2 = new BigDecimal("2.23163693");
        BigDecimal result2 = system.calculate(x2, DEFAULT_PRECISION);
        assertEquals(expected2, result2);
    }

     private void configureMock(BasicFunction mock, String functionName) {
        when(mock.calculate(any(BigDecimal.class), any(BigDecimal.class)))
            .thenAnswer(invocation -> {
                BigDecimal x = invocation.getArgument(0);
                return functionValues.get(functionName).getOrDefault(x, ZERO);
            });
    }

    @Test
    void shouldThrowWhenNullArgument() {
        FinalFunction system = new FinalFunction();
        assertThrows(NullPointerException.class, 
            () -> system.calculate(null, DEFAULT_PRECISION));
    }
    
    @Test
    void shouldThrowWhenNullPrecision() {
        FinalFunction system = new FinalFunction();
        assertThrows(NullPointerException.class, 
            () -> system.calculate(ONE, null));
    }
    
    static Stream<Arguments> invalidPrecisions() {
        return Stream.of(
            Arguments.of(ZERO),
            Arguments.of(new BigDecimal("-0.1")),
            Arguments.of(new BigDecimal("1E-100"))
        );
    }
    
    

    @Test
    void testNotAcceptNullArgument() {
        final FinalFunction system = new FinalFunction();
        assertThrows(NullPointerException.class, () -> system.calculate(null, DEFAULT_PRECISION));
    }

    @Test
    void testNotAcceptNullPrecision() {
        final FinalFunction system = new FinalFunction();
        assertThrows(NullPointerException.class, () -> system.calculate(new BigDecimal(-2), null));
    }

    @ParameterizedTest
    @MethodSource("illegalPrecisions")
    void testNotAcceptIncorrectPrecisions(final BigDecimal precision) {
        final FinalFunction system = new FinalFunction();
        assertThrows(ArithmeticException.class, () -> system.calculate(new BigDecimal(-2), precision));
    }

    @Test
    void testNotAcceptZeroArgument() {
        final FinalFunction system = new FinalFunction();
        assertThrows(ArithmeticException.class, () -> system.calculate(ZERO, DEFAULT_PRECISION));
    }

    @Test
    void testNotAcceptOneArgument() {
        final FinalFunction system = new FinalFunction();
        assertThrows(ArithmeticException.class, () -> system.calculate(ONE, DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForPositiveValue() {
        final FinalFunction system = new FinalFunction();
        final BigDecimal expected = new BigDecimal("2.23163693");
        assertEquals(expected, system.calculate(new BigDecimal(2), DEFAULT_PRECISION));
    }

    @Test
    void testCalculateForNegativeValue() {
        final FinalFunction system = new FinalFunction();
        final BigDecimal expected = new BigDecimal("-2.73560392");
        assertEquals(expected, system.calculate(new BigDecimal(-1), DEFAULT_PRECISION));
    }


    @Test
    void testCalculateWithMocks() {

        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockSec, "sec");
        configureMock(mockLn, "ln");
        configureMock(mockLog2, "log2");
        configureMock(mockLog3, "log3");
        configureMock(mockLog5, "log5");

        // when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        // when(mockCos.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.54030231"));
        // when(mockTan.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.55740770"));
        // when(mockCot.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.64209262"));
        // when(mockCsc.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.18839511"));
        // when(mockSec.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("1.85081570"));
        // when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));
        // when(mockLog2.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.13750352"));
        // when(mockLog3.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.08675506"));
        // when(mockLog5.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.05921954"));


        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, mockSec, mockLn, mockLog2, mockLog3, mockLog5);

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(2), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }


    @Test
    void testCalculateWithMocksWithoutLog2() {
        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockSec, "sec");
        configureMock(mockLn, "ln");
        configureMock(mockLog3, "log3");
        configureMock(mockLog5, "log5");
        // when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        // when(mockCos.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.54030231"));
        // when(mockTan.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.55740770"));
        // when(mockCot.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.64209262"));
        // when(mockCsc.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.18839511"));
        // when(mockSec.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("1.85081570"));
        // when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));
        // when(mockLog3.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.08675506"));
        // when(mockLog5.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.05921954"));

        
        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, mockSec, mockLn, new Log(2), mockLog3, mockLog5);

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(2), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }


    @Test
    void testCalculateWithMocksWithoutLog2AndLog3() {
        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockSec, "sec");
        configureMock(mockLn, "ln");
        configureMock(mockLog5, "log5");
        
        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, mockSec, mockLn, new Log(2),new Log(3), mockLog5);

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(2), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }


    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndLog5() {
        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockSec, "sec");
        configureMock(mockLn, "ln");
        
        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, mockSec, mockLn, new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(2), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndLog5AndSec() {
        configureMock(mockSin, "sin");
        configureMock(mockCos, "cos");
        configureMock(mockTan, "tan");
        configureMock(mockCot, "cot");
        configureMock(mockCsc, "csc");
        configureMock(mockLn, "ln");

        
        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, mockCsc, new Sec(), mockLn, new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(2), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }


    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCsc() {
        when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        when(mockCos.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.54030231"));
        when(mockTan.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.55740770"));
        when(mockCot.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.64209262"));
        when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));

        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, mockCot, new Csc(), new Sec(), mockLn, new Log(2), new Log(3), new Log(5));
        
        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    
    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCscAndCot() {
        when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        when(mockCos.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.54030231"));
        when(mockTan.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-1.55740770"));
        when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));

        FinalFunction system = new FinalFunction(mockSin, mockTan, mockCos, new Cot(), new Csc(), new Sec(), mockLn, new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCscAndCotAndTan() {
        when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        when(mockCos.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.54030231"));
        when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));

        FinalFunction system = new FinalFunction(mockSin, new Tan(), mockCos, new Cot(), new Csc(mockSin), new Sec(), mockLn,new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCscAndCotAndTanAndCos() {
        when(mockSin.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("-0.84147098"));
        when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));

        FinalFunction system = new FinalFunction(mockSin, new Tan(), new Cos(), new Cot(), new Csc(), new Sec(new Cos()), mockLn, new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCscAndCotAndTanAndCosAndSin() {
        when(mockLn.calculate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(new BigDecimal("0.09531016"));

        FinalFunction system = new FinalFunction(new Sin(), new Tan(), new Cos(), new Cot(), new Csc(), new Sec(new Cos()), mockLn, new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }

    @Test
    void testCalculateWithMocksWithoutLog2AndLog3AndSecAndCscAndCotAndTanAndCosAndSinAndLn() {
        FinalFunction system = new FinalFunction(new Sin(), new Tan(), new Cos(), new Cot(), new Csc(), new Sec(new Cos()), new Ln(), new Log(2), new Log(3), new Log(5));

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        BigDecimal result1 = system.calculate(new BigDecimal(1.1), DEFAULT_PRECISION);
        BigDecimal expected1 = new BigDecimal("2.23163693");

        assertEquals(expected, result);
        assertEquals(expected1, result1);

    }


    private static Stream<Arguments> illegalPrecisions() {
            return Stream.of(
                    Arguments.of(valueOf(1)),
                    Arguments.of(valueOf(0)),
                    Arguments.of(valueOf(1.01)),
                    Arguments.of(valueOf(-0.01))
            );
        }
    
    @Test
    void testCalculateWithSpies() {
        final Cos cos = new Cos(spySin);
        final Cos spyCos = spy(cos);
        final Ln ln = new Ln();
        FinalFunction system = new FinalFunction(
                spySin,
                new Tan(spySin, spyCos),
                spyCos,
                new Cot(spySin, spyCos),
                new Csc(spySin),
                new Sec(spyCos),
                ln,
                new Log(ln, 2),
                new Log(ln, 3),
                new Log(ln, 5)
        );

        BigDecimal result = system.calculate(new BigDecimal(-1), DEFAULT_PRECISION);
        BigDecimal expected = new BigDecimal("-2.73560392");
        assertEquals(expected, result);

        verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    }

    private final FinalFunction function = new FinalFunction();
    private static final MathContext MC = new MathContext(20);
    private static final BigDecimal PI = BigDecimalMath.pi(MC);
    private static final BigDecimal TWO_PI = PI.multiply(valueOf(2));
    private static final BigDecimal PRECISION = new BigDecimal("0.0001");

    static Stream<Arguments> negativeInputs() {
        return Stream.of(
                Arguments.of(valueOf(-1)),
                Arguments.of(valueOf(-2)),
                Arguments.of(valueOf(-3))
        );
    }

    @ParameterizedTest
    @MethodSource("negativeInputs")
    void testFunctionIsPeriodicWith2Pi(BigDecimal x) {
        BigDecimal y1 = function.calculate(x, PRECISION);

        BigDecimal x2 = x.subtract(TWO_PI);
        BigDecimal y2 = function.calculate(x2, PRECISION);

        assertEquals(y1.doubleValue(), y2.doubleValue(), PRECISION.doubleValue(),
                "Функция должна быть периодической с периодом 2π");
    }

    @Test
    void testPeriodicityBoundary() {
        BigDecimal x = valueOf(-4);
        BigDecimal y1 = function.calculate(x, PRECISION);
        BigDecimal y2 = function.calculate(x.subtract(TWO_PI), PRECISION);
        assertEquals(y1.doubleValue(), y2.doubleValue(), PRECISION.doubleValue());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1.0, -2.5, -3.8})
    void testMultiplePeriods(double xVal) {
        BigDecimal x = valueOf(xVal);
        BigDecimal y1 = function.calculate(x, PRECISION);

        for (int n = 1; n <= 3; n++) {
            BigDecimal xN = x.subtract(TWO_PI.multiply(valueOf(n)));
            BigDecimal yN = function.calculate(xN, PRECISION);
            assertEquals(y1.doubleValue(), yN.doubleValue(), PRECISION.doubleValue());
        }
    }

    @Test
    void testNonPeriodicForPositiveX() {
        BigDecimal x1 = valueOf(2);
        BigDecimal x2 = x1.add(TWO_PI);

        BigDecimal y1 = function.calculate(x1, PRECISION);
        BigDecimal y2 = function.calculate(x2, PRECISION);
        System.out.println(y1);
        System.out.println(y2);

        assertNotEquals(y1.doubleValue(), y2.doubleValue(), PRECISION.doubleValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.0001", "0.0001"})
    void testTransitionNearZero(String xStr) {
        BigDecimal x = new BigDecimal(xStr);
        assertDoesNotThrow(
                () -> function.calculate(x, PRECISION),
                "Функция должна работать вблизи x = 0"
        );
    }

    @Test
    void testConsistencyForPeriodicPoints() {
        BigDecimal x1 = new BigDecimal("-1");
        BigDecimal x2 = x1.subtract(TWO_PI);

        BigDecimal y1 = function.calculate(x1, PRECISION);
        BigDecimal y2 = function.calculate(x2, PRECISION);

        assertEquals(
                y1.doubleValue(),
                y2.doubleValue(),
                PRECISION.doubleValue(),
                "Функция должна давать одинаковые результаты для x и x + 2π"
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {-Math.PI, -2*Math.PI, 0})
    void testDiscontinuityPoints(double x) {
        assertThrows(ArithmeticException.class,
                () -> function.calculate(new BigDecimal(x), PRECISION),
                "Функция должна выбрасывать исключение в точках разрыва"
        );
    }

    @Test
    void testBehaviorAtNegativeInfinity() {
        BigDecimal x = new BigDecimal("-1e100");
        BigDecimal result = function.calculate(x, PRECISION);

        assertTrue(result.abs().compareTo(new BigDecimal("1e100")) < 0,
                "Функция не должна уходить в бесконечность при x → -∞");
    }

    @Test
    void testAsimptotNearByZero() {
        FinalFunction system = new FinalFunction(new Sin(), new Tan(), new Cos(), new Cot(), new Csc(), new Sec(), new Ln(), new Log(2), new Log(3), new Log(5));

        BigDecimal expected = new BigDecimal("-1001987448444540638078177131.58459800");
        assertEquals(expected, system.calculate(new BigDecimal("-0.001"), new BigDecimal("0.00000001")));
    }

    @Test
    void testLogarithmicExtremum() {
        BigDecimal x = new BigDecimal("1.5");
        BigDecimal left = function.calculate(x.subtract(PRECISION), PRECISION);
        BigDecimal center = function.calculate(x, PRECISION);
        BigDecimal right = function.calculate(x.add(PRECISION), PRECISION);

        assertTrue(center.compareTo(left) * center.compareTo(right) == 0,
                "В точке x=1.5 должен быть экстремум");
    }

    private static final Object[][] TEST_POINTS = {
        {-9.8, 10.5174},
        {-9.6, 1162388.7817}, 
        {-6.6, 47913.3584}, 
        {-6.4, -240902240.7926 },
        {-3.4, 11265.4542}, 
        {-3.2, 76540714841.0027},
        {-3.0, -116125799.5456},
        {-0.4, 15190.9688},
        {-0.2, -825238.6318}
    };

    @ParameterizedTest
    @MethodSource("testPointsProvider")
    void testSpecificPoints(double x, double expectedY) {
        BigDecimal xBig = new BigDecimal(String.valueOf(x));
        BigDecimal result = assertDoesNotThrow(
                () -> function.calculate(xBig, new BigDecimal("0.00000001")),
                "Ошибка вычисления в точке x=" + x
        );
        assertEquals(expectedY, result.doubleValue(), PRECISION.doubleValue(),
                "Несоответствие в точке x=" + x
        );
    }

    static Stream<Arguments> testPointsProvider() {
        return Arrays.stream(TEST_POINTS)
                .map(arr -> Arguments.of(arr[0], arr[1]));
    }
}
