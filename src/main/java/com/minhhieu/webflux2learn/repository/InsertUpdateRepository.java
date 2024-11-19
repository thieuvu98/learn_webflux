package com.minhhieu.webflux2learn.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface InsertUpdateRepository<T> {
    <S extends T> Mono<S> insert(S entity);

    <S extends T> Mono<S> update(S entity);

    <S extends T> Flux<S> insertAll(Flux<S> entities);

    <S extends T> Flux<S> updateAll(Flux<S> entities);
}
