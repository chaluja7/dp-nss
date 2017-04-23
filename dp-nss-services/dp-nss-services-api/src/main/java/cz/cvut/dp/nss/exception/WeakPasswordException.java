package cz.cvut.dp.nss.exception;

/**
 * @author jakubchalupa
 * @since 23.04.17
 */
public class WeakPasswordException extends Exception {

    private static final long serialVersionUID = 3785344120822398273L;

    public WeakPasswordException() {
        super("Slabe heslo.");
    }

    public WeakPasswordException(String message) {
        super(message);
    }

}
