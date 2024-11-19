package com.minhhieu.webflux2learn.thirdparty.impl;

import com.minhhieu.webflux2learn.exception.BusinessException;
import com.minhhieu.webflux2learn.model.filter.PersonFilter;
import com.minhhieu.webflux2learn.model.response.PersonResponse;
import com.minhhieu.webflux2learn.thirdparty.PersonClient;
import com.minhhieu.webflux2learn.util.ErrorCode;
import com.minhhieu.webflux2learn.util.Response;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;

@Log4j2
public class PersonClientImpl implements PersonClient {
    private static final ParameterizedTypeReference<Response<List<PersonResponse>>> LIST_RESPONSE_READER = new ParameterizedTypeReference<>() {
    };
    private final WebClient webClient;

    public PersonClientImpl(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .responseTimeout(Duration.ofMillis(10_000));
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Mono<Response<List<PersonResponse>>> getPersons(PersonFilter filter, Integer page, Integer size) {
        Mono<Response<List<PersonResponse>>> res = webClient.get()
                .uri(uri -> uri.path("/persons")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("name", filter.getName())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(LIST_RESPONSE_READER)
                        .flatMap(body -> {
                            if ("MANAGE-4043".equals(body.getMeta().getCode())) {
                                return Mono.error(new BusinessException(ErrorCode.INVALID_BODY, ""));
                            }
                            return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVICE_ERROR, "Service Internal Error"));
                        }))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Server Internal Error")))
                .bodyToMono(LIST_RESPONSE_READER);
        return res.onErrorMap(this::handle);
    }
    // a.onErrorResume(t -> Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Server Internal Error")));

    private Throwable handle(Throwable throwable) {
        if (throwable.getCause() instanceof ReadTimeoutException) {
            return new BusinessException(ErrorCode.TIME_OUT, null, throwable);
        } else {
            return throwable;
        }
    }

}
