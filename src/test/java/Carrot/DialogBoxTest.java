package carrot;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class DialogBoxTest {

    @Test
    void classLoads() throws Exception {
        Class<?> clazz = Class.forName("carrot.DialogBox");

        assertNotNull(clazz);
    }

    @Test
    void factoryMethods_existAndAreStatic() throws Exception {
        Method userDialog = DialogBox.class.getDeclaredMethod("getUserDialog",
                String.class, javafx.scene.image.Image.class);
        Method carrotDialog = DialogBox.class.getDeclaredMethod("getCarrotDialog",
                String.class, javafx.scene.image.Image.class);

        assertTrue(Modifier.isStatic(userDialog.getModifiers()));
        assertTrue(Modifier.isStatic(carrotDialog.getModifiers()));
    }
}
