package com.minhhieu.webflux2learn.validator;

import com.minhhieu.webflux2learn.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;

@Component
public class Validator {
    private final org.springframework.validation.Validator validator;

    public Validator(org.springframework.validation.Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T object) {
        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        validator.validate(object, errors);
        if (errors.hasErrors()) {
            return Mono.error(new ValidationException("Invalid input", errors.getAllErrors()));
        }
        return Mono.just(object);
    }
}
