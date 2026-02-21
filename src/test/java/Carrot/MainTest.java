package carrot;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void constructor_initializesCarrotField() throws Exception {
        Main main = new Main();
        Field carrotField = Main.class.getDeclaredField("carrot");
        carrotField.setAccessible(true);
        Object carrot = carrotField.get(main);

        assertNotNull(carrot);
    }
}
