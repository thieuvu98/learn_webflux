package com.minhhieu.webflux2learn.util;

import org.springframework.data.relational.core.query.Criteria;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.relational.core.query.Criteria.from;
import static org.springframework.data.relational.core.query.Criteria.where;

public class CriteriaUtil {
    private CriteriaUtil() {

    }

    public static Criteria search(List<String> attributes, String value) {
        if (value == null || attributes.isEmpty()) {
            return null;
        }
        return doSearch(attributes, value);
    }

    public static Criteria doSearch(List<String> attributes, String value) {
        var criteria = attributes.stream()
                .map(att -> doContain(att, value))
                .toList();
        return from(criteria);
    }

    public static Criteria contain(String attribute, String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return doContain(attribute, value);
    }

    private static Criteria doContain(String attribute, String value) {
        return where(attribute).like("%" + value + "%").ignoreCase(true);
    }

    public static <T> Criteria equal(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).is(value);
    }

    public static <T> Criteria notEqual(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).not(value);
    }

    public static <T> Criteria greaterThanOrEqualTo(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).greaterThanOrEquals(value);
    }

    public static <T> Criteria greaterThan(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).greaterThan(value);
    }

    public static <T> Criteria lessThanOrEqualTo(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).lessThanOrEquals(value);
    }

    public static <T> Criteria lessThan(String attribute, T value) {
        if (value == null) {
            return null;
        }
        return where(attribute).lessThan(value);
    }

    public static <T> Criteria in(String attribute, List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        return where(attribute).in(values);
    }

    public static <T> Criteria isNull(String attribute) {
        return where(attribute).isNull();
    }

    public static <T> Criteria isNotNull(String attribute) {
        return where(attribute).isNotNull();
    }
}
