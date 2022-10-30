package exceptions;

public class ComandoInvalidoException extends Exception {

    public ComandoInvalidoException(String e) {
        super(e);
    }

    public ComandoInvalidoException() {
        super("\nComando Invalido. \nPorfavor Inserte uno valido.");
    }
}
