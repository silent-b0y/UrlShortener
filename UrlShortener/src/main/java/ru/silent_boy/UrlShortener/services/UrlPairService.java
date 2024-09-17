package ru.silent_boy.UrlShortener.services;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.RedirectView;
import ru.silent_boy.UrlShortener.dto.LongUrlDTO;
import ru.silent_boy.UrlShortener.dto.ShortUrlDTO;
import ru.silent_boy.UrlShortener.exceptions.UrlExpiredException;
import ru.silent_boy.UrlShortener.exceptions.UrlNotFoundException;
import ru.silent_boy.UrlShortener.models.UrlPair;
import ru.silent_boy.UrlShortener.repositories.UrlPairRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UrlPairService {
    @Value("${baseUrl}")
    private String baseUrl;
    private final UrlPairRepository urlPairRepository;

    @Autowired
    public UrlPairService(UrlPairRepository urlPairRepository) {
        this.urlPairRepository = urlPairRepository;
    }

    @Transactional
    public void save(UrlPair urlPair) {
        urlPair.setCreatedAt(LocalDateTime.now());
        urlPairRepository.save(urlPair);
    }

    @Transactional
    public ShortUrlDTO createShortUrl(UrlPair urlPair) {
        Optional<UrlPair> existingUrlPair = urlPairRepository.findByLongUrl(urlPair.getLongUrl());
        if (existingUrlPair.isPresent()) {
            UrlPair presentUrlPair = existingUrlPair.get();
            this.save(presentUrlPair);
            return new ShortUrlDTO(presentUrlPair.getShortUrl());
        }

        byte[] sha256 = Hashing.sha256()
                .hashString(urlPair.getLongUrl(), StandardCharsets.UTF_8)
                .asBytes();
        String shortUrl = BaseEncoding.base64Url().encode(sha256).substring(0, 7);

        urlPair.setShortUrl(baseUrl + shortUrl);
        this.save(urlPair);
        return new ShortUrlDTO(urlPair.getShortUrl());
    }

    public LongUrlDTO getLongUrl(UrlPair urlPair) {
        Optional<UrlPair> existingUrlPair = urlPairRepository.findByShortUrl(urlPair.getShortUrl());
        if (existingUrlPair.isPresent()) {
            return new LongUrlDTO(existingUrlPair.get().getLongUrl());
        }
        log.error("Short url {} doesn't exist!", urlPair.getShortUrl());
        throw new UrlNotFoundException("shortUrl - Short url doesn't exist!");
    }

    public RedirectView redirectToLongUrl(String shortUrl) {
        Optional<UrlPair> existingUrlPair = urlPairRepository.findByShortUrl(shortUrl);
        if (existingUrlPair.isPresent()) {
            UrlPair presentUrlPair = existingUrlPair.get();
            if (presentUrlPair.isExpired()) {
                log.error("Short url {} is expired!", presentUrlPair.getShortUrl());
                throw new UrlExpiredException("shortUrl - Short url is expired!");
            }
            return new RedirectView(presentUrlPair.getLongUrl());
        }
        log.error("Short url {} doesn't exist!", shortUrl);
        throw new UrlNotFoundException("shortUrl - Short url doesn't exist!");
    }
}
