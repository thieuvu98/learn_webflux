package com.minhhieu.webflux2learn.util;

import com.minhhieu.webflux2learn.exception.BusinessErrorCode;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Log4j2
public class ErrorCode {
    public static final BusinessErrorCode INTERNAL_SERVER_ERROR =
            new BusinessErrorCode(5000, "Internal server error", 500);

    public static final BusinessErrorCode INTERNAL_SERVICE_ERROR =
            new BusinessErrorCode(5001, "Internal service error", 500);

    public static final BusinessErrorCode CANT_FORGOT_PASSWORD =
            new BusinessErrorCode(5002, "Can't forgot password", 500);

    public static final BusinessErrorCode TIME_OUT =
            new BusinessErrorCode(5003, "Timeout", 500);

    public static final BusinessErrorCode INVALID_PARAMETERS =
            new BusinessErrorCode(4000, "Invalid parameters", 400);

    public static final BusinessErrorCode UNAUTHORIZED =
            new BusinessErrorCode(4001, "You need to login to to access this resource", 401);

    public static final BusinessErrorCode FORBIDDEN =
            new BusinessErrorCode(4002, "You don't have permission to to access this resource", 403);

    public static final BusinessErrorCode TEMPLATE_INVALID =
            new BusinessErrorCode(4007, "Template is invalid", 400);

    public static final BusinessErrorCode DUPLICATE_PARAMS =
            new BusinessErrorCode(4009, "Duplicate params", 400);

    public static final BusinessErrorCode EXTRACT_FILE_FAIL =
            new BusinessErrorCode(4003, "Fail to extract file", 403);

    public static final BusinessErrorCode INVALID_TOKEN =
            new BusinessErrorCode(4043, "Invalid token", 400);

    public static final BusinessErrorCode INVALID_BODY =
            new BusinessErrorCode(4044, "Invalid body", 400);

    public static final BusinessErrorCode INVALID_PERSON =
            new BusinessErrorCode(4045, "Invalid person", 400);


    static {
        Set<Integer> codes = new HashSet<>();
        var duplications = Arrays.stream(ErrorCode.class.getDeclaredFields())
                .filter(f -> Modifier.isStatic(f.getModifiers()) && f.getType().equals(BusinessErrorCode.class))
                .map(f -> {
                    try {
                        return ((BusinessErrorCode) f.get(null)).getCode();
                    } catch (IllegalAccessException e) {
                        log.error("can't load error code into map", e);
                        throw new RuntimeException(e);
                    }
                })
                .filter(code -> !codes.add(code))
                .toList();
        if (!duplications.isEmpty()) {
            throw new RuntimeException("Duplicate error code: " + duplications);
        }
    }

    private ErrorCode() {
    }
}
