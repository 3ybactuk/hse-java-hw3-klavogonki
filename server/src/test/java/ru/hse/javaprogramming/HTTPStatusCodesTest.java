package ru.hse.javaprogramming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HTTPStatusCodesTest {

    @Test
    public void testGetCode_OK() {
        int expectedCode = 200;
        int actualCode = HTTPStatusCodes.OK.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_CREATED() {
        int expectedCode = 201;
        int actualCode = HTTPStatusCodes.CREATED.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_ACCEPTED() {
        int expectedCode = 202;
        int actualCode = HTTPStatusCodes.ACCEPTED.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_NO_CONTENT() {
        int expectedCode = 204;
        int actualCode = HTTPStatusCodes.NO_CONTENT.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_BAD_REQUEST() {
        int expectedCode = 400;
        int actualCode = HTTPStatusCodes.BAD_REQUEST.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_UNAUTHORIZED() {
        int expectedCode = 401;
        int actualCode = HTTPStatusCodes.UNAUTHORIZED.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_FORBIDDEN() {
        int expectedCode = 403;
        int actualCode = HTTPStatusCodes.FORBIDDEN.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_NOT_FOUND() {
        int expectedCode = 404;
        int actualCode = HTTPStatusCodes.NOT_FOUND.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_INTERNAL_SERVER_ERROR() {
        int expectedCode = 500;
        int actualCode = HTTPStatusCodes.INTERNAL_SERVER_ERROR.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCode_SERVICE_UNAVAILABLE() {
        int expectedCode = 503;
        int actualCode = HTTPStatusCodes.SERVICE_UNAVAILABLE.getCode();

        Assertions.assertEquals(expectedCode, actualCode);
    }
}