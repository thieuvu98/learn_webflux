package com.minhhieu.webflux2learn.mapper;

import com.minhhieu.webflux2learn.model.Person;
import com.minhhieu.webflux2learn.model.request.CreatePersonRequest;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;

@Mapper
public interface PersonMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "createdAt")
    Person map(Long id, CreatePersonRequest source, OffsetDateTime createdAt);

    PersonResponse map(Person person);

}
