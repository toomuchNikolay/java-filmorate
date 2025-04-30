package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidArgument(final MethodArgumentNotValidException e) {
        return buildResponse(HttpStatus.BAD_REQUEST.value(), e);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND.value(), e);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedData(final DuplicatedDataException e) {
        return buildResponse(HttpStatus.CONFLICT.value(), e);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleValidation(final ValidationException e) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }

    private ErrorResponse buildResponse(Integer status, Exception e) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status)
                .message(e.getMessage())
                .build();
    }
}
