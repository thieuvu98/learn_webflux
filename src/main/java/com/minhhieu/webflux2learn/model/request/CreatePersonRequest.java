package com.minhhieu.webflux2learn.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePersonRequest {
    @NotBlank
    String name;
    @NotNull
    LocalDate dob;
    @NotBlank
    String status;
}
