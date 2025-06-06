package ru.yandex.practicum.filmorate.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.sql.SQLException;
import java.time.Instant;

@RestControllerAdvice
public class ErrorHandler {
    private static final String CONSTRAINT_EMAIL_TABLE_USERS = "unique_email";
    private static final String CONSTRAINT_LOGIN_TABLE_USERS = "unique_login";
    private static final String VIOLATION_NOT_NULL = "23502";
    private static final String VIOLATION_UNIQUE = "23505";

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrity(final DataIntegrityViolationException e) {
        if (e.getRootCause() != null && e.getRootCause() instanceof SQLException sqlE) {
            if (VIOLATION_NOT_NULL.equals(sqlE.getSQLState())) {
                return buildResponse(HttpStatus.BAD_REQUEST.value(),
                        new ValidationException("Поле не может быть пустым"));
            } else if (VIOLATION_UNIQUE.equals(sqlE.getSQLState())) {
                String message = "Запись с указанными данными уже существует";
                if (sqlE.getMessage().toLowerCase().contains(CONSTRAINT_EMAIL_TABLE_USERS)) {
                    message = "Указанный email уже используется";
                } else if (sqlE.getMessage().toLowerCase().contains(CONSTRAINT_LOGIN_TABLE_USERS)) {
                    message = "Указанный login уже используется";
                }
                return buildResponse(HttpStatus.CONFLICT.value(),
                        new DuplicatedDataException(message));
            }
        }
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
