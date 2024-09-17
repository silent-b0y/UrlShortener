package ru.silent_boy.UrlShortener.exceptions;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String msg) {
        super(msg);
    }
}
