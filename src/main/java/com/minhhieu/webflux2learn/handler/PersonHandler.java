package com.minhhieu.webflux2learn.handler;

import com.minhhieu.webflux2learn.annotation.Permission;
import com.minhhieu.webflux2learn.exception.BusinessException;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.model.request.UpdatePersonRequest;
import com.minhhieu.webflux2learn.service.PersonService;
import com.minhhieu.webflux2learn.util.Constant;
import com.minhhieu.webflux2learn.util.ErrorCode;
import com.minhhieu.webflux2learn.util.Response;
import com.minhhieu.webflux2learn.validator.PersonValidator;
import com.minhhieu.webflux2learn.validator.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PersonHandler {
    private final PersonService personService;
    private final PersonValidator personValidator;
    private final Validator validator;

    public PersonHandler(PersonService personService, PersonValidator personValidator, Validator validator) {
        this.personService = personService;
        this.personValidator = personValidator;
        this.validator = validator;
    }

    public Mono<ServerResponse> getPersons(ServerRequest request) {
        int page = request.queryParam("page").map(Integer::parseInt).orElse(0);
        int size = request.queryParam("size").map(Integer::parseInt).orElse(10);
        return personService.getPersons(PersonFilter.of(request), PageRequest.of(page, size))
                .flatMap(Response::ok);
    }

    // We only set the annotation here, because it will trigger when route layer calls to handle layer
//    @Permission(subject = Constant.CREATE_PERSON, action = "#request.status", object = "#request.name")
    public Mono<ServerResponse> activeAnnotation(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreatePersonRequest.class)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_BODY, "Body is null")))
                .flatMap(validator::validate)
                .flatMap(personValidator::validate)
                .flatMap(personService::create)
                .then(Response.ok());
    }


    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreatePersonRequest.class)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_BODY, "Body is null")))
                .flatMap(validator::validate)
                .flatMap(personValidator::validate)
                .flatMap(personService::create)
                .then(Response.ok());
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(UpdatePersonRequest.class)
                .map(it -> it.setId(id))
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_BODY, "Body is null")))
                .flatMap(validator::validate)
                .flatMap(personService::update)
                .flatMap(Response::ok);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        long id = Long.parseLong(serverRequest.pathVariable("id"));
        return personService.delete(id)
                .then(Response.ok());
    }
}
