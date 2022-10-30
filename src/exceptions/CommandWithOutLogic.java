package exceptions;

public class CommandWithOutLogic extends Throwable {
    public CommandWithOutLogic() {
    }

    public CommandWithOutLogic(String message) {
        super(message);
    }

    public CommandWithOutLogic(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandWithOutLogic(Throwable cause) {
        super(cause);
    }

    public CommandWithOutLogic(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
