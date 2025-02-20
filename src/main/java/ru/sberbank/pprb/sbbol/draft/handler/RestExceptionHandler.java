package ru.sberbank.pprb.sbbol.draft.handler;

import org.hibernate.ObjectNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.sberbank.pprb.sbbol.draft.dto.response.ErrorData;
import ru.sberbank.pprb.sbbol.draft.exception.EntryNotFoundException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest httpRequest) {
        LOG.error("Нарушение ограничений в БД", ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST,
                ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(". ")),
                httpRequest.getRequestURL());
    }

    @ExceptionHandler({EntryNotFoundException.class, EntityNotFoundException.class, ObjectNotFoundException.class})
    protected ResponseEntity<Object> handleObjectNotFoundException(Exception ex, HttpServletRequest httpRequest) {
        LOG.error("Объект не найден", ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), httpRequest.getRequestURL());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex, HttpServletRequest httpRequest) {
        LOG.error("Необработанное исключение", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), httpRequest.getRequestURL());
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers, @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        return buildResponseEntity(status,
                ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(". ")),
                ((ServletWebRequest) request).getRequest().getRequestURL());
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, @NotNull HttpHeaders headers,
                                                             @NotNull HttpStatus status, @NotNull WebRequest request) {
        return buildResponseEntity(status, ex.getLocalizedMessage(), ((ServletWebRequest) request).getRequest().getRequestURL());
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String errorDesc, StringBuffer requestUrl) {
        ErrorData errorData = new ErrorData(String.valueOf(status.value()), status.name(), errorDesc);
        String url = requestUrl.toString().replaceAll("[\n\r\t]", "_");
        LOG.error("Error when calling \"{}\": {}", url, errorData);
        return new ResponseEntity<>(errorData, status);
    }

}
