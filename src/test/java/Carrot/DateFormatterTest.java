package carrot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;


class DateFormatterTest {

    @Test
    void testFormatter_withTime_success() {
        String input = "2026-02-08 15:30";
        LocalDateTime result = LocalDateTime.parse(input, DateFormatter.FORMATTER);
        assertEquals(2026, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(8, result.getDayOfMonth());
        assertEquals(15, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    void testFormatter_withoutTime_defaultsToMidnight() {
        String input = "2026-02-08";
        LocalDateTime result = LocalDateTime.parse(input, DateFormatter.FORMATTER);
        assertEquals(8, result.getDayOfMonth());
        assertEquals(0, result.getHour(), "Hour should default to 00");
        assertEquals(0, result.getMinute(), "Minute should default to 00");
    }

    @Test
    void testOutput_formattingCorrectly() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 8, 18, 45);
        String formatted = dateTime.format(DateFormatter.OUTPUT);
        assertEquals("08-02-2026 18:45", formatted);
    }

    @Test
    void testOutput_withMidnight_formattingCorrectly() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 8, 0, 0);
        String formatted = dateTime.format(DateFormatter.OUTPUT);
        assertEquals("08-02-2026 00:00", formatted);
    }
}
