package cz.cvut.dp.nss.exception;

/**
 * Hesla se neshoduji.
 *
 * @author jakubchalupa
 * @since 23.04.17
 */
public class PasswordsDoNotMatchException extends Exception {

    private static final long serialVersionUID = 6405273757748212386L;

    public PasswordsDoNotMatchException() {
        super("Hesla se neshoduji.");
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }

}
