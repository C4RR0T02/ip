package carrot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class MainWindowTest {

    @Test
    void classLoads() throws Exception {
        Class<?> clazz = Class.forName("carrot.MainWindow");

        assertNotNull(clazz);
    }

    @Test
    void expectedMethodsExist() throws Exception {
        Method initialize = MainWindow.class.getDeclaredMethod("initialize");
        Method setCarrot = MainWindow.class.getDeclaredMethod("setCarrot", Carrot.class);
        Method handleUserInput = MainWindow.class.getDeclaredMethod("handleUserInput");

        assertNotNull(initialize);
        assertNotNull(setCarrot);
        assertNotNull(handleUserInput);
    }
}
