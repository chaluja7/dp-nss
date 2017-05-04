package cz.cvut.dp.nss.exception;

/**
 * Spatne prihlasovaci udaje.
 *
 * @author jakubchalupa
 * @since 06.03.17
 */
public class BadCredentialsException extends Exception {

    private static final long serialVersionUID = 1274383793204037527L;

    public BadCredentialsException() {
        super("Spatne prihlasovaci udaje.");
    }

    public BadCredentialsException(String message) {
        super(message);
    }

}
