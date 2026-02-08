package Carrot;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateFormatter {

    /** DateTimeFormatter to parse date and time in "yyyy-MM-dd HH:mm" format
     * Time part is optional; if not provided, defaults to 00:00
     */
    public static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();


    /** DateTimeFormatter to output date and time in "dd-MM-yyyy HH:mm" format
     * Time part is optional; if not provided, defaults to 00:00
     */
    public static final DateTimeFormatter OUTPUT = new DateTimeFormatterBuilder()
            .appendPattern("dd-MM-yyyy")
            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();
}
