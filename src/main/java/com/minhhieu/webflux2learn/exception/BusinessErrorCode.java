package com.minhhieu.webflux2learn.exception;

import lombok.Value;

@Value
public class BusinessErrorCode {
    int code;
    String message;
    int httpStatus;
}
