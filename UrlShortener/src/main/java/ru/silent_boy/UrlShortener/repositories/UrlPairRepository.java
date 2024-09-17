package ru.silent_boy.UrlShortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.silent_boy.UrlShortener.models.UrlPair;

import java.util.Optional;

@Repository
public interface UrlPairRepository extends JpaRepository<UrlPair, Integer> {
    Optional<UrlPair> findByLongUrl(String longUrl);
    Optional<UrlPair> findByShortUrl(String shortUrl);
}
