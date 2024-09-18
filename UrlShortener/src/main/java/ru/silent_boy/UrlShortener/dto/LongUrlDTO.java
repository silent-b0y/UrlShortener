package ru.silent_boy.UrlShortener.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LongUrlDTO {
    @Schema(
            description = "long url",
            name = "longUrl",
            type = "string",
            example = "https://web.telegram.org/k/"
    )
    @NotEmpty(message = "Url must not be empty!")
    private String longUrl;

    public LongUrlDTO(String longUrl) {
        this.longUrl = longUrl;
    }
}
