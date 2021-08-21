package vn.elca.training.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.elca.training.model.exception.*;
import vn.elca.training.model.response.MessageReponse;
import vn.elca.training.util.StatusCode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public Log logger = LogFactory.getLog(getClass());

    @ExceptionHandler({
            ProjectNumberCanNotBeChangedException.class,
            ProjectNumberAlreadyExistedExcetion.class,
            ProjectStatusNewCanNotBeDeleteException.class,
            InValidVisasException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageReponse> badRequest(Exception ex) {
        if (ex instanceof ProjectNumberCanNotBeChangedException) {
            logger.info(ex.getMessage(), ex);
            MessageReponse mp = new MessageReponse(StatusCode.PNUM_CHANGE.getCode(), Collections.singletonList("Project number can't be changed."));
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
        if (ex instanceof ProjectStatusNewCanNotBeDeleteException) {
            logger.info(ex.getMessage(), ex);
            MessageReponse mp = new MessageReponse(StatusCode.PDELETE_NOTNEW.getCode(), Collections.singletonList("Projects have new status can't be deleted."));
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
        if (ex instanceof ProjectNumberAlreadyExistedExcetion) {
            logger.info(ex.getMessage(), ex);
            MessageReponse mp = new MessageReponse(StatusCode.PNUM_EXISTED.getCode(), Collections.singletonList("The project number already existed. Please select a different project number"));
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        } else {
            logger.info(ex.getMessage(), ex);
            MessageReponse mp = new MessageReponse(StatusCode.PEMP_INVAL.getCode(), Collections.singletonList(ex.getMessage()));
            return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler({
            ProjectNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageReponse> notFound(Exception ex) {
        logger.info(ex.getMessage(), ex);
        MessageReponse mp = new MessageReponse(StatusCode.PNOT_FOUND.getCode(), Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<MessageReponse> notFoundAbstract(Exception ex) {
        logger.info(ex.getMessage(), ex);
        MessageReponse mp = new MessageReponse(StatusCode.NOT_FOUND.getCode(), Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(mp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            StaleObjectStateException.class
    )
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<MessageReponse> conflict(StaleObjectStateException ex) {
        logger.info(ex.getMessage(), ex);
        MessageReponse mp = new MessageReponse(StatusCode.PCONCUR_UPD.getCode(), Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(mp, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> objectErrors = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        MessageReponse mp = new MessageReponse(StatusCode.PENDD_INVAL.getCode(), objectErrors);
        objectErrors.forEach(p -> logger.info(p, ex));
        return new ResponseEntity<>(mp, headers, HttpStatus.BAD_REQUEST);
    }

}
