package com.minhhieu.webflux2learn.router;

import com.minhhieu.webflux2learn.handler.PersonHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
public class HttpRouter {

    // https://docs.spring.io/spring-framework/reference/web/webflux-functional.html#webflux-fn-handler-filter-function
    @Bean
    public RouterFunction<ServerResponse> route(PersonHandler personHandler) {
        return RouterFunctions.route()
                .path("/persons", builder -> builder.nest(
                        accept(APPLICATION_JSON), builder1 -> builder1
                                .GET(personHandler::getPersons)
                                .POST(personHandler::create)
                                .PUT("/{id}", personHandler::update)
                                .DELETE("/{id}", personHandler::delete)
                ))
                .build();
    }
}
