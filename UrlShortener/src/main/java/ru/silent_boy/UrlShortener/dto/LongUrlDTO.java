package ru.silent_boy.UrlShortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LongUrlDTO {
    @NotEmpty(message = "Url must not be empty!")
    private String longUrl;

    public LongUrlDTO(String longUrl) {
        this.longUrl = longUrl;
    }
}
