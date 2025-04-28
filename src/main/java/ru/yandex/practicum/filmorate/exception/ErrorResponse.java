package ru.yandex.practicum.filmorate.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private Instant timestamp;
    private Integer status;
    private String message;
}
