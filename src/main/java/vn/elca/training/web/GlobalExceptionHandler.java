package vn.elca.training.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.elca.training.model.response.MessageReponse;
import vn.elca.training.util.StatusCode;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageReponse> handleAllException(Exception ex) {
        logger.info("Internal server error", ex);
        MessageReponse mp = new MessageReponse(StatusCode.INTERNAL_ERROR.getCode(), Collections.singletonList(ex.getLocalizedMessage()));
        return new ResponseEntity<>(mp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
