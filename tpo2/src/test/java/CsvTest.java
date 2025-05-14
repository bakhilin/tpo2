import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ifmo.lab.Csv;
import ru.ifmo.lab.functions.BasicFunction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CsvTest {
    @Mock
    private BasicFunction mockFunction;

    @TempDir
    Path tempDir;

    @Test
    void testWrite() throws IOException {
        when(mockFunction.calculate(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(new BigDecimal("1.0")) // Возвращаем 1.0 для всех значений, кроме 0.5
                .thenThrow(new ArithmeticException("Invalid value")) // Выбрасываем исключение для 0.5
                .thenReturn(new BigDecimal("2.0")); // Возвращаем 2.0 для остальных значений

        File csvFile = tempDir.resolve("test_skip_invalid.csv").toFile();

        Csv.write(
                csvFile.getAbsolutePath(),
                mockFunction,
                new BigDecimal("0"),
                new BigDecimal("1"),
                new BigDecimal("0.5"),
                new BigDecimal("0.0001")
        );

        assertTrue(csvFile.exists());

        String fileContent = Files.readString(csvFile.toPath());
        assertTrue(fileContent.contains("1.0,2.0"));
        assertFalse(fileContent.contains("0.5"));
    }
}