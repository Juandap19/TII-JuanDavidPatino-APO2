package exceptions;

public class IncorrectFormatException extends Throwable {
    public IncorrectFormatException() {
    }

    public IncorrectFormatException(String message) {
        super(message);
    }

    public IncorrectFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFormatException(Throwable cause) {
        super(cause);
    }
}
