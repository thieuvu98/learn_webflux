package com.minhhieu.webflux2learn.model.filter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Arrays;
import java.util.List;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonFilter {
    Long id;
    String name;
    List<String> statuses;


    public static PersonFilter of(ServerRequest request) {
        var id = request.queryParam("id").map(Long::parseLong).orElse(null);
        var name = request.queryParam("name").orElse(null);
        var statusQuery = request.queryParam("statuses").orElse("");
        var statuses = Arrays.stream(statusQuery.split(","))
                .filter(StringUtils::hasText)
                .map(String::trim)
                .toList();
        return new PersonFilter().setId(id)
                .setName(name)
                .setStatuses(statuses);
    }
}
