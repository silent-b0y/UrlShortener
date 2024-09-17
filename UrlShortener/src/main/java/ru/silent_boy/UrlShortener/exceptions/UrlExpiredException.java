package ru.silent_boy.UrlShortener.exceptions;

public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String msg) {
        super(msg);
    }
}
