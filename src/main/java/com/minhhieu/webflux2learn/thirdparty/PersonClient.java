package com.minhhieu.webflux2learn.thirdparty;

import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import com.minhhieu.webflux2learn.util.Response;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonClient {
    Mono<Response<List<PersonResponse>>> getPersons(PersonFilter filter, Integer page, Integer size);
}
