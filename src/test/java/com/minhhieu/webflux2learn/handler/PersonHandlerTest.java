package com.minhhieu.webflux2learn.handler;

import com.minhhieu.webflux2learn.ApplicationTest;
import com.minhhieu.webflux2learn.model.PersonStatus;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.util.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

@Log4j2
class PersonHandlerTest extends ApplicationTest {
    @Autowired
    private ApplicationContext context;
    private WebTestClient webTestClient;
    @Autowired
    private DatabaseClient DatabaseClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void getPersons() {
        DatabaseClient.sql("truncate table person restart identity").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (1, 'Dam Trinh Hung', '2000-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (2, 'Nguyen Van An', '2001-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (3, 'Luong Thi Hien', '2002-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (4, 'Truong Huyen Anh', '1999-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (5, 'Vu Van Thieu', '1998-02-03', 'ACTIVATE', '2023-12-13 08:02:02.635393 +00:00', '2023-12-13 08:02:02.635393 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (6, 'Nguyen Thi Nhung', '1998-02-03', 'SUSPENDED', '2023-12-13 08:03:31.815812 +00:00', '2023-12-13 08:03:31.815812 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (7, 'Nguyen Duc Tung 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (8, 'Nguyen Thu Huong 1', null, 'ACTIVATE', '2023-12-13 09:33:04.440109 +00:00', '2023-12-13 18:44:28.432904 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (13, 'Huynh Duong Gia', null, 'ACTIVATE', '2023-12-13 18:23:58.553652 +00:00', '2023-12-13 18:23:58.553652 +00:00')").fetch().rowsUpdated().block();

        webTestClient.get().uri("/persons?page=0&size=3&name=ANh").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.meta.code").isEqualTo(Response.PREFIX + 200)
                .jsonPath("$.data.size()").isEqualTo(1)
        ;
    }

    @Test
    void create() {
        DatabaseClient.sql("truncate table person restart identity").fetch().rowsUpdated().block();
        DatabaseClient.sql("INSERT INTO public.person (id, name, dob, status, created_at, updated_at) VALUES (1, 'Dam Trinh Hung', '2000-12-12', 'ACTIVATE', '2023-12-12 17:06:54.970000 +00:00', '2023-12-12 17:06:54.970000 +00:00')").fetch().rowsUpdated().block();
        DatabaseClient.sql("SELECT setVal('person_id_seq', 3)").fetch().rowsUpdated().block();

        webTestClient.post().uri("/persons").accept(MediaType.APPLICATION_JSON)
                .bodyValue(new CreatePersonRequest().setName("Vu Van Tha").setDob(LocalDate.now()).setStatus(PersonStatus.ACTIVATE))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.meta.code").isEqualTo(Response.PREFIX + 200)
        ;
    }
}