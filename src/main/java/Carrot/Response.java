package carrot;

/**
 * Represents a response from Carrot with a message and command type.
 */
public class Response {

    /**
     * Enum representing the type of command associated with the response.
     */
    public enum CommandType { HELP, FIND, LIST, TODO, DEADLINE, EVENT, DELETE, MARK, UNMARK, BYE, INVALID }

    private final String message;
    private final CommandType commandType;

    /**
     * Constructor for Response class
     * @param commandType The type of command associated with the response
     * @param message The message of the response
     */
    public Response(String commandType, String message) {
        assert commandType != null : "commandType must not be null";
        assert message != null : "message must not be null";
        this.message = message;
        this.commandType = CommandType.valueOf(commandType);
    }

    /**
     * Gets the message of the response
     * @return The message of the response
     */
    public String getMessage() {
        assert this.message != null : "message should not be null";
        return this.message;
    }

    /**
     * Gets the command type of the response
     * @return The command type of the response
     */
    public CommandType getCommandType() {
        assert this.commandType != null : "commandType should not be null";
        return this.commandType;
    }

    @Override
    public String toString() {
        assert this.commandType != null : "commandType should not be null";
        assert this.message != null : "message should not be null";
        return this.commandType + this.message;
    }
}
