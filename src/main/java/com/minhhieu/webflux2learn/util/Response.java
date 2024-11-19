package com.minhhieu.webflux2learn.util;

import com.minhhieu.webflux2learn.exception.BusinessErrorCode;
import com.minhhieu.webflux2learn.exception.FieldViolation;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;


@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response<T> {
    public static String PREFIX = "MANAGE-";
    Meta meta;
    T data;

    public static Response<Void> ofFailed(BusinessErrorCode errorCode, String message, List<FieldViolation> errors) {
        Meta meta = new Meta()
                .setCode(PREFIX + errorCode.getCode())
                .setMessage(message != null ? message : errorCode.getMessage())
                .setErrors(errors);
        Response<Void> res = new Response<>();
        res.setMeta(meta);
        return res;
    }

    public static <T> Mono<ServerResponse> ok() {
        Meta meta = new Meta()
                .setCode(PREFIX + 200)
                .setMessage("Success");
        Response<T> res = new Response<>();
        res.setMeta(meta);
        return ServerResponse.ok()
                .bodyValue(res);
    }

    public static <T> Mono<ServerResponse> ok(T value) {
        Meta meta = new Meta()
                .setCode(PREFIX + 200)
                .setMessage("Success");
        Response<T> res = new Response<>();
        res.setMeta(meta);
        res.setData(value);
        return ServerResponse.ok()
                .bodyValue(res);
    }

    public static <T> Mono<ServerResponse> ok(Page<T> page) {
        Meta meta = new Meta()
                .setCode(PREFIX + 200)
                .setPage(page.getNumber())
                .setSize(page.getSize())
                .setTotal(page.getTotalElements())
                .setMessage("Success");
        Response<List<T>> res = new Response<>();
        res.setMeta(meta);
        res.setData(page.getContent());
        return ServerResponse.ok()
                .bodyValue(res);
    }


}

