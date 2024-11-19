package com.minhhieu.webflux2learn.exception;

import lombok.Value;

@Value
public class FieldViolation {
    String field;
    String description;
}
