package cz.cvut.dp.nss.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jakubchalupa
 * @since 24.02.17
 */
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public abstract class AbstractController {

    protected ResponseEntity<?> getResponseCreated(Object body, String location) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, location);
        return new ResponseEntity<>(body, httpHeaders, HttpStatus.CREATED);
    }

}
