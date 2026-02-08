package Carrot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class CarrotTest {

    @TempDir
    Path tempDir;

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void testMain_withCustomPath_doesNotTouchProductionData() {
        Path testFilePath = tempDir.resolve("test_tasks.txt");
        String testPathString = testFilePath.toString();

        String simulatedInput = "todo Save the world" + System.lineSeparator() + "bye";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        try {
            Carrot.main(new String[]{testPathString});

            File testFile = testFilePath.toFile();
            assertTrue(testFile.exists(), "Should have created the file in the temp directory");

            Storage testStorage = new Storage(testPathString);
            assertEquals(1, testStorage.load().size(), "Should have saved 1 task to the temp file");
        } catch (Exception e) {
            fail("Test crashed: " + e.getMessage());
        }
    }
}
