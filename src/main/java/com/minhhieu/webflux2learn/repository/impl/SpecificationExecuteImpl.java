package com.minhhieu.webflux2learn.repository.impl;

import com.minhhieu.webflux2learn.repository.SpecificationExecute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Query.query;

@Component
public class SpecificationExecuteImpl<E> implements SpecificationExecute<E> {
    private final R2dbcEntityTemplate ret;

    public SpecificationExecuteImpl(R2dbcEntityTemplate ret) {
        this.ret = ret;
    }

    public Flux<E> findAll(Criteria criteria, Pageable pageable, Class<E> entityType) {
        return ret.select(query(criteria).with(pageable), entityType);
    }

    public Mono<Long> count(Criteria criteria) {
        return ret.count(query(criteria), Long.class);
    }

}
