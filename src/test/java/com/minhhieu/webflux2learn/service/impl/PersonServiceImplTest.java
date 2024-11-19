package com.minhhieu.webflux2learn.service.impl;

import com.minhhieu.webflux2learn.ApplicationTest;
import com.minhhieu.webflux2learn.model.Person;
import com.minhhieu.webflux2learn.model.PersonStatus;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@SuppressWarnings("all")
class PersonServiceImplTest extends ApplicationTest {
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private DatabaseClient databaseClient;

    @Test
    void getPersons() {
        databaseClient.sql("truncate table person restart identity").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (1, 'Dam Trinh Hung', '2000-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (2, 'Nguyen Van An', '2001-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (3, 'Luong Thi Hien', '2002-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (4, 'Truong Huyen Anh', '1999-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (5, 'Vu Van Thieu', '1998-02-03', 'ACTIVATE', '2023-12-13 08:02:02.635393 +00:00', '2023-12-13 08:02:02.635393 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (6, 'Nguyen Thi Nhung', '1998-02-03', 'SUSPENDED', '2023-12-13 08:03:31.815812 +00:00', '2023-12-13 08:03:31.815812 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (7, 'Nguyen Duc Tung 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (8, 'Nguyen Thu Huong 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00')").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (13, 'Huynh Duong Gia', null, 'ACTIVATE', '2023-12-13 18:23:58.553652 +00:00', '2023-12-13 18:23:58.553652 +00:00')").fetch().rowsUpdated().block();

        var filter = new PersonFilter().setName("ANh");
        var personPage = personService.getPersons(filter, PageRequest.of(0, 10));

        StepVerifier.create(personPage)
                .expectNextMatches(page -> {
                    assertEquals(1, page.getTotalElements());
                    var person = page.getContent().getFirst();
                    assertTrue(person.getName().toLowerCase().contains(filter.getName().toLowerCase()));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void create() {
        databaseClient.sql("truncate table person restart identity").fetch().rowsUpdated().block();
        databaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (1, 'Dam Trinh Hung', '2000-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();

        var result = personService.create(new CreatePersonRequest().setName("Vu Van Tha").setDob(LocalDate.now()).setStatus(PersonStatus.ACTIVATE));
        StepVerifier.create(result)
                .expectError(DuplicateKeyException.class)
                .verify();
    }

    @Test
    void getPersonInCache() {
        var person = new Person().setId(10L).setName("Hoang Van Thuong").setStatus(PersonStatus.ACTIVATE);
        var oldPerson = personService.putPersonToCache(person);

        StepVerifier.create(oldPerson)
                .verifyComplete();

        var personMono = personService.getPersonInCache(10L);
        StepVerifier.create(personMono)
                .expectNextMatches(expect -> {
                    assertEquals(person.getId(), expect.getId());
                    assertEquals(person.getName(), expect.getName());
                    assertEquals(person.getStatus(), expect.getStatus());
                    return true;
                })
                .verifyComplete();

        personMono = personService.getPersonInCache(5L);
        StepVerifier.create(personMono)
                .verifyComplete();
    }
}