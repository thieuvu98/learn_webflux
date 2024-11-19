package com.minhhieu.webflux2learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Webflux2LearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(Webflux2LearnApplication.class, args);
    }

}
