package ru.silent_boy.UrlShortener.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String msg) {
        super(msg);
    }
}
