package com.minhhieu.webflux2learn.repository.impl;

import com.minhhieu.webflux2learn.repository.InsertUpdateRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class InsertUpdateRepositoryImpl<T> implements InsertUpdateRepository<T> {
    private final R2dbcEntityOperations entityOperations;

    public InsertUpdateRepositoryImpl(R2dbcEntityOperations entityOperations) {
        this.entityOperations = entityOperations;
    }

    @Override
    public <S extends T> Mono<S> insert(S entity) {
        return entityOperations.insert(entity);
    }

    @Override
    public <S extends T> Mono<S> update(S entity) {
        return entityOperations.update(entity);
    }

    @Override
    public <S extends T> Flux<S> insertAll(Flux<S> entities) {
        return entities.concatMap(this::insert);
    }

    @Override
    public <S extends T> Flux<S> updateAll(Flux<S> entities) {
        return entities.concatMap(this::update);
    }
}
