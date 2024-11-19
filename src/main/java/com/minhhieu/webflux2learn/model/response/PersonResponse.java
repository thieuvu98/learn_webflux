package com.minhhieu.webflux2learn.model.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonResponse {
    Long id;
    String name;
    LocalDate dob;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
