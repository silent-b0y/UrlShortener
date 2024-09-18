package ru.silent_boy.UrlShortener.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.silent_boy.UrlShortener.dto.LongUrlDTO;
import ru.silent_boy.UrlShortener.dto.ShortUrlDTO;
import ru.silent_boy.UrlShortener.exceptions.handlers.ErrorResponse;

@Tag(name = "UrlController - url shortener controller", description = "controller for processing short and long urls")
public interface IUrlController {
    @Operation(
            summary = "creates short url",
            description = "creates short url from long url and return created short url",
            responses = {
                    @ApiResponse(
                            description = "short url created",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ShortUrlDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "invalid long url",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<ShortUrlDTO> createShortUrl(@RequestBody @Valid LongUrlDTO longUrlDTO,
                                                      BindingResult bindingResult);

    @Operation(
            summary = "returns long url",
            description = "returns long url from short url",
            responses = {
                    @ApiResponse(
                            description = "long url returned",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LongUrlDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "invalid short url",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "short url doesn't exist",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<LongUrlDTO> getLongUrl(@RequestBody @Valid ShortUrlDTO shortUrlDTO,
                                          BindingResult bindingResult);

    @Operation(
            summary = "redirects to long url",
            description = "redirects to long url from short url",
            parameters = @Parameter(
                    name = "shortUrl",
                    description = "short url for redirect",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            description = "redirects to long url",
                            responseCode = "302"
                    ),
                    @ApiResponse(
                            description = "short url is expired",
                            responseCode = "410",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "short url doesn't exist",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    RedirectView redirectToLongUrl(@PathVariable("shortUrl") String shortUrl);
}
