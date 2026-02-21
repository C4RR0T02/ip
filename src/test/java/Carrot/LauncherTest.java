package carrot;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class LauncherTest {

    @Test
    void mainMethod_existsAndIsStatic() throws Exception {
        Method main = Launcher.class.getDeclaredMethod("main", String[].class);

        assertNotNull(main);
        assertTrue(Modifier.isStatic(main.getModifiers()));
    }
}
