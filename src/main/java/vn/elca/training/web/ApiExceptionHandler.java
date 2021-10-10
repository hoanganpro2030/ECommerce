package vn.elca.training.web;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.elca.training.model.entity.HttpResponse;
import vn.elca.training.model.exception.*;
import vn.elca.training.model.response.MessageReponse;
import vn.elca.training.util.StatusCode;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
//@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public Log logger = LogFactory.getLog(getClass());
    public static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    public static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred while processing the request";
    public static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    public static final String ACCOUNT_DISABLED = "Your account has been disabled. If this is an error, please contact administration";
    public static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    public static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ERROR_PATH = "/error";

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase()
            , message.toUpperCase()), httpStatus);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisableException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsExceptionException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MissingInformationRequiredException.class)
    public ResponseEntity<HttpResponse> missingInformationRequiredException(MissingInformationRequiredException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNameExistException.class)
    public ResponseEntity<HttpResponse> userNameExistException(UserNameExistException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundExeption.class)
    public ResponseEntity<HttpResponse> emailNotFoundExeption(EmailNotFoundExeption exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException exception) {
//        return createHttpResponse(HttpStatus.BAD_REQUEST, "This page was not found");
//    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<HttpResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
//        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
//        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> noResultException(NoResultException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, INTERNAL_SERVER_ERROR_MESSAGE);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> ioException(IOException exception) {
        log.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

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

    @ExceptionHandler(
            NotEnoughProductQuantityException.class
    )
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageReponse> notEnoughQuantityProduct(NotEnoughProductQuantityException ex) {
        logger.info(ex.getMessage(), ex);
        MessageReponse mp = new MessageReponse(StatusCode.QUANT_P_NOT_ENOUGH.getCode(), Collections.singletonList(ex.getMessage()), ex.getProductName());
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
