package carrot;

/**
 * Custom exception class for Carrot application
 */
public class CarrotException extends Exception {

    /**
     * Constructor for CarrotException
     * @param message Error message
     */
    public CarrotException(String message) {
        super(message);
    }
}
