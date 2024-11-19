package com.minhhieu.webflux2learn.thirdparty.impl;

import com.minhhieu.webflux2learn.ApplicationTest;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.thirdparty.PersonClient;
import com.minhhieu.webflux2learn.util.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class PersonClientImplTest extends ApplicationTest {
    @Autowired
    private PersonClient personClient;

    @Test
    void getPersons() {
        var personsMono = personClient.getPersons(new PersonFilter().setName("ANh"), 0, 10);

        StepVerifier.create(personsMono)
                .expectNextMatches(res -> {
                    assertEquals(Response.PREFIX + 200, res.getMeta().getCode());
                    assertEquals(1, res.getData().size());
                    var person = res.getData().getFirst();
                    assertTrue(person.getName().toLowerCase().contains("ANh".toLowerCase()));
                    return true;
                })
                .verifyComplete();
    }
}