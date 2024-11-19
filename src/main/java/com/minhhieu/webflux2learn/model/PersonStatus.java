package com.minhhieu.webflux2learn.model;

import java.util.List;

public class PersonStatus {
    public static final String ACTIVATE = "ACTIVATE";
    public static final String SUSPENDED = "SUSPENDED";
    public static final String PROCESSING = "PROCESSING";
    private static final List<String> statuses = List.of(ACTIVATE, SUSPENDED, PROCESSING);

    private PersonStatus() {

    }

    public static boolean contain(String status) {
        return statuses.contains(status);
    }
}
