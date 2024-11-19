package com.minhhieu.webflux2learn.config;

import com.minhhieu.webflux2learn.mapper.PersonMapper;
import com.minhhieu.webflux2learn.mapper.PersonMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public PersonMapper personMapper() {
        return new PersonMapperImpl();
    }
}
