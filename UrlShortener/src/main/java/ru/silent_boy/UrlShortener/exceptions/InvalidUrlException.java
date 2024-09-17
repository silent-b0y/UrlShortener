package ru.silent_boy.UrlShortener.exceptions;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String msg) {
        super(msg);
    }
}
