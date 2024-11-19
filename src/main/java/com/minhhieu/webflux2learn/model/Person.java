package com.minhhieu.webflux2learn.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Data
@Accessors(chain = true)
public class Person {
    @Id
    Long id;
    String name;
    LocalDate dob;
    String status;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
