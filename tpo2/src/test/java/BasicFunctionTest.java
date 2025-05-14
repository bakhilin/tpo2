import java.math.BigDecimal;
import static java.math.BigDecimal.ONE;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.ifmo.lab.functions.BasicFunction;
import ru.ifmo.lab.logarithm.Ln;
import ru.ifmo.lab.logarithm.Log;
import ru.ifmo.lab.trigonometry.Cos;
import ru.ifmo.lab.trigonometry.Cot;
import ru.ifmo.lab.trigonometry.Csc;
import ru.ifmo.lab.trigonometry.Sec;
import ru.ifmo.lab.trigonometry.Sin;
import ru.ifmo.lab.trigonometry.Tan;

class BasicFunctionTest {

    private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.000001");

    @ParameterizedTest
    @MethodSource("functions")
    void testNotAcceptNullArgument(final BasicFunction function) {
        assertThrows(NullPointerException.class, () -> function.calculate(null, DEFAULT_PRECISION));
    }

    @ParameterizedTest
    @MethodSource("functions")
    void testNotAcceptNullPrecision(final BasicFunction function) {
        assertThrows(NullPointerException.class, () -> function.calculate(ONE, null));
    }

    private static Stream<Arguments> functions() {
        return Stream.of(
                Arguments.of(new Sin()),
                Arguments.of(new Cos()),
                Arguments.of(new Tan()),
                Arguments.of(new Cot()),
                Arguments.of(new Sec()),
                Arguments.of(new Csc()),

                Arguments.of(new Ln()),
                Arguments.of(new Log(2)),
                Arguments.of(new Log(3))
        );
    }
}