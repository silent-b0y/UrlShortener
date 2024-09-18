package ru.silent_boy.UrlShortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShortUrlDTO {
    @Schema(
            description = "short url",
            name = "shortUrl",
            type = "string",
            example = "http://localhost:8080/ABCDEFG"
    )
    @NotEmpty(message = "Url must not be empty!")
    private String shortUrl;

    public ShortUrlDTO(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
