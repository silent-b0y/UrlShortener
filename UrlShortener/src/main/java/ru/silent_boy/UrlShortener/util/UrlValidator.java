package ru.silent_boy.UrlShortener.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class UrlValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String url = (String) target;
        String name = errors.getObjectName();
        String field;
        if (name.startsWith("long")) {
            field = "longUrl";
        } else {
            field = "shortUrl";
        }
        if (!isValidURL(url)) {
            errors.rejectValue(field, "", "Invalid url!");
        }
    }

    boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
