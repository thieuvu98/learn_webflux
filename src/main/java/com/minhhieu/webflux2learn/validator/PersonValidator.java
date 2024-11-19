package com.minhhieu.webflux2learn.validator;

import com.minhhieu.webflux2learn.exception.BusinessException;
import com.minhhieu.webflux2learn.model.PersonStatus;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.util.ErrorCode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PersonValidator {

    public PersonValidator() {

    }

    public Mono<CreatePersonRequest> validate(CreatePersonRequest request) {
        if (!PersonStatus.contain(request.getStatus())) {
            return Mono.error(new BusinessException(ErrorCode.INVALID_PARAMETERS, "Status is invalid, value: " + request.getStatus()));
        }
        return Mono.just(request);
    }
}
