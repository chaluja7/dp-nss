package cz.cvut.dp.nss.exception;

/**
 * @author jakubchalupa
 * @since 22.10.16
 */
public class BadRequestException extends Exception {

    private static final long serialVersionUID = 6276656261583782682L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
