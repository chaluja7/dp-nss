package cz.cvut.dp.nss.controller;

import cz.cvut.dp.nss.exception.BadRequestException;
import cz.cvut.dp.nss.exception.ForbiddenException;
import cz.cvut.dp.nss.exception.ResourceNotFoundException;
import cz.cvut.dp.nss.exception.UnauthorizedException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

/**
 * @author jakubchalupa
 * @since 24.02.17
 */
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public abstract class AbstractController {

    private static final Logger LOGGER = Logger.getLogger(AbstractController.class);

    protected <T> ResponseEntity<T> getResponseCreated(T body) {
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseWrapper> handleNotFoundException(Exception e) {
        LOGGER.error("", e);
        return new ResponseEntity<>(new ExceptionResponseWrapper("resource not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponseWrapper> handleUnauthorizedException(Exception e) {
        LOGGER.error("", e);
        return new ResponseEntity<>(new ExceptionResponseWrapper("unauthorized"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponseWrapper> handleForbiddenException(Exception e) {
        LOGGER.error("", e);
        return new ResponseEntity<>(new ExceptionResponseWrapper("forbidden"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({BadRequestException.class, DataIntegrityViolationException.class, PersistenceException.class, ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponseWrapper> handleBadRequestException(Exception e) {
        LOGGER.error("", e);
        return new ResponseEntity<>(new ExceptionResponseWrapper(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseWrapper> handleException(Exception e) {
        LOGGER.error("", e);
        return new ResponseEntity<>(new ExceptionResponseWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * odpoved pro exception
     */
    private class ExceptionResponseWrapper {

        private final String message;

        private ExceptionResponseWrapper(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }

}
