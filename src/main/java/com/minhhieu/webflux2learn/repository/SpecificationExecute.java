package com.minhhieu.webflux2learn.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpecificationExecute<E> {

    Flux<E> findAll(Criteria criteria, Pageable pageable, Class<E> entityType);

    Mono<Long> count(Criteria criteria);
}
