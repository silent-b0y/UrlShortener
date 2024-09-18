package ru.silent_boy.UrlShortener.exceptions.handlers;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    @Schema(example = "Too many requests!")
    private String message;
    @Schema(example = "2024-07-04T09:54:56.604283300Z")
    private LocalDateTime timestamp;
}
