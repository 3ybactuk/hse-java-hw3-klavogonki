package ru.hse.javaprogramming;

public enum HTTPStatusCodes {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private final int code;

    HTTPStatusCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
