package ru.silent_boy.UrlShortener.controllers;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.silent_boy.UrlShortener.dto.LongUrlDTO;
import ru.silent_boy.UrlShortener.dto.ShortUrlDTO;
import ru.silent_boy.UrlShortener.exceptions.InvalidUrlException;
import ru.silent_boy.UrlShortener.exceptions.TooManyRequestsException;
import ru.silent_boy.UrlShortener.models.UrlPair;
import ru.silent_boy.UrlShortener.services.UrlPairService;
import ru.silent_boy.UrlShortener.util.UrlValidator;

import java.time.Duration;

import static ru.silent_boy.UrlShortener.util.ErrorsUtil.returnErrorsMessage;

@Slf4j
@RestController
public class UrlController implements IUrlController {
    @Value("${baseUrl}")
    private String baseUrl;
    private final UrlValidator urlValidator;
    private final UrlPairService urlPairService;
    private final ModelMapper modelMapper;
    private final Bucket bucket;

    @Autowired
    public UrlController(UrlPairService urlPairService, UrlValidator urlValidator, ModelMapper modelMapper) {
        this.urlPairService = urlPairService;
        this.urlValidator = urlValidator;
        this.modelMapper = modelMapper;
        this.bucket = Bucket.builder()
                .addLimit(Bandwidth.classic(100, Refill.greedy(100, Duration.ofMinutes(1))))
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ShortUrlDTO> createShortUrl(@RequestBody @Valid LongUrlDTO longUrlDTO,
                                                      BindingResult bindingResult) {
        if (bucket.tryConsume(1)) {
            urlValidator.validate(longUrlDTO.getLongUrl(), bindingResult);
            if (bindingResult.hasErrors()) {
                String errorMsg = returnErrorsMessage(bindingResult);
                log.error(errorMsg);
                throw new InvalidUrlException(errorMsg);
            }
            UrlPair urlPair = convertToUrlPair(longUrlDTO);
            ShortUrlDTO shortUrlDTO = urlPairService.createShortUrl(urlPair);
            return new ResponseEntity<>(shortUrlDTO, HttpStatus.CREATED);
        }
        log.error("Too many requests!");
        throw new TooManyRequestsException("Too many requests!");
    }

    @GetMapping("/longUrl")
    public ResponseEntity<LongUrlDTO> getLongUrl(@RequestBody @Valid ShortUrlDTO shortUrlDTO,
                                             BindingResult bindingResult) {
        urlValidator.validate(shortUrlDTO.getShortUrl(), bindingResult);
        if (bindingResult.hasErrors()) {
            String errorMsg = returnErrorsMessage(bindingResult);
            log.error(errorMsg);
            throw new InvalidUrlException(errorMsg);
        }
        UrlPair urlPair = convertToUrlPair(shortUrlDTO);
        LongUrlDTO longUrlDTO = urlPairService.getLongUrl(urlPair);
        return ResponseEntity.ok(longUrlDTO);
    }

    @GetMapping("/{shortUrl}")
    public RedirectView redirectToLongUrl(@PathVariable("shortUrl") String shortUrl) {
        return urlPairService.redirectToLongUrl(baseUrl + shortUrl);
    }

    private UrlPair convertToUrlPair(LongUrlDTO longUrlDTO) {
        return modelMapper.map(longUrlDTO, UrlPair.class);
    }

    private UrlPair convertToUrlPair(ShortUrlDTO shortUrlDTO) {
        return modelMapper.map(shortUrlDTO, UrlPair.class);
    }
}
