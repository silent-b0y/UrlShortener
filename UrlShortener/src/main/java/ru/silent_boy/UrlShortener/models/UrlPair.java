package ru.silent_boy.UrlShortener.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "Url")
@Getter
@Setter
@NoArgsConstructor
public class UrlPair {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return Duration.between(this.getCreatedAt(), LocalDateTime.now()).toMinutes() >= 10;
    }
}
