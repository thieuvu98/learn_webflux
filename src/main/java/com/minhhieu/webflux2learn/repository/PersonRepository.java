package com.minhhieu.webflux2learn.repository;

import com.minhhieu.webflux2learn.model.Person;
import com.minhhieu.webflux2learn.model.request.UpdatePersonRequest;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, Long>, SpecificationExecute<Person>,
        InsertUpdateRepository<Person> {
    @Query("SELECT nextval('person_id_seq')")
    Mono<Long> nextId();

    @Query("select nextval('person_id_seq') from generate_series(1, :amount)")
    Flux<Long> nextIds(int amount);

    @Query("update person set name = :#{#request.name}, updated_at = :now where id = :#{#request.id} and status = 'ACTIVATE' returning *")
    Mono<Person> update(UpdatePersonRequest request, OffsetDateTime now);

    @Query("delete from person where id = :id returning id")
    Mono<Long> delete(Long id);
}
