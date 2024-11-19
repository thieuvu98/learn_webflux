package com.minhhieu.webflux2learn.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.minhhieu.webflux2learn.annotation.Permission;
import com.minhhieu.webflux2learn.exception.BusinessException;
import com.minhhieu.webflux2learn.mapper.PersonMapper;
import com.minhhieu.webflux2learn.model.Person;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.model.request.UpdatePersonRequest;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import com.minhhieu.webflux2learn.notifier.PersonNotifier;
import com.minhhieu.webflux2learn.repository.PersonRepository;
import com.minhhieu.webflux2learn.service.internal.PersonServiceInternal;
import com.minhhieu.webflux2learn.util.Constant;
import com.minhhieu.webflux2learn.util.ErrorCode;
import com.minhhieu.webflux2learn.util.Filters;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;


@Service
@Log4j2
public class PersonServiceImpl implements PersonServiceInternal {
    private static final String PERSON_CACHE_NAME_SPACE = "PERSON_CACHE_NAME_SPACE";
    private final PersonMapper personMapper;
    private final PersonRepository personRepository;
    private final PersonNotifier personNotifier;
    private final IMap<Long, Person> personCache;

    public PersonServiceImpl(HazelcastInstance hazelcastInstance,
                             PersonRepository personRepository, PersonMapper personMapper, PersonNotifier personNotifier) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.personNotifier = personNotifier;
        this.personCache = hazelcastInstance.getMap(PERSON_CACHE_NAME_SPACE);
    }

    public Mono<Page<PersonResponse>> getPersons(PersonFilter filter, Pageable pageable) {
        var criteria = Filters.toCriteria(filter);
        return personRepository.findAll(criteria, pageable, Person.class)
                .map(personMapper::map)
                .collectList()
                .flatMap(persons -> {
                    if (persons.size() < pageable.getPageSize()) {
                        var total = persons.size() + (pageable.getPageSize() * pageable.getPageNumber());
                        return Mono.just(new PageImpl<>(persons, pageable, total));
                    } else {
                        return personRepository.count(criteria)
                                .flatMap(total -> Mono.just(new PageImpl<>(persons, pageable, total)));
                    }
                });
    }

    @Override
    @Permission(subject = Constant.CREATE_PERSON, action = "#request.status", object = "#request.name")
    public Mono<Void> create(CreatePersonRequest request) {
        return personRepository.nextId()
                .map(id -> personMapper.map(id, request, OffsetDateTime.now()))
                .flatMap(personRepository::insert)
                .then();
    }

    @Override
    public Mono<PersonResponse> update(UpdatePersonRequest request) {
        return personRepository.update(request, OffsetDateTime.now())
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PERSON, "Invalid person id " + request.getId())))
                .map(personMapper::map);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return personRepository.delete(id)
                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.INVALID_PERSON, "Invalid person id " + id)))
                .then();
    }

    Mono<Person> getPersonInCache(Long id) {
        return Mono.justOrEmpty(personCache.get(id));
    }

    Mono<Person> putPersonToCache(Person person) {
        return Mono.justOrEmpty(personCache.put(person.getId(), person, 6000, TimeUnit.MILLISECONDS));
    }


}
