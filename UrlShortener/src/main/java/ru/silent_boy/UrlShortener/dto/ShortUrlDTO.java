package ru.silent_boy.UrlShortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShortUrlDTO {
    @NotEmpty(message = "Url must not be empty!")
    private String shortUrl;

    public ShortUrlDTO(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
