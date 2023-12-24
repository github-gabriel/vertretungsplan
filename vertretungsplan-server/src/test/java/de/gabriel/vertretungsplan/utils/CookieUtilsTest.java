package de.gabriel.vertretungsplan.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CookieUtilsTest {

    @Test
    @DisplayName("Should parse cookie string into a map")
    public void testParseCookie() {
        String cookieString = "cookie1=value1; cookie2=value2";
        Map<String, String> cookieMap = CookieUtils.parseCookie(cookieString);

        assertEquals(2, cookieMap.size());
        assertEquals("value1", cookieMap.get("cookie1"));
        assertEquals("value2", cookieMap.get("cookie2"));
    }

    @Test
    @DisplayName("Should get cookie value by name")
    public void testGetCookieValue() {
        String cookieString = "cookie1=value1; cookie2=value2";
        String cookieValue = CookieUtils.getCookieValue(cookieString, "cookie1");

        assertEquals("value1", cookieValue);
    }

    @Test
    @DisplayName("Should check if cookie is HttpOnly")
    public void testIsHttpOnly() {
        String cookieString = "cookie1=value1; HttpOnly";
        boolean result = CookieUtils.isHttpOnly(cookieString);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should get default path when Path is not specified")
    public void testGetDefaultPath() {
        String cookieString = "cookie1=value1";
        String path = CookieUtils.getPath(cookieString);

        assertEquals("/", path);
    }

    @Test
    @DisplayName("Should get Max-Age value as an integer")
    public void testGetMaxAge() {
        String cookieString = "cookie1=value1; Max-Age=3600";
        int maxAge = CookieUtils.getMaxAge(cookieString);

        assertEquals(3600, maxAge);
    }

    @Test
    @DisplayName("Should handle invalid Max-Age value")
    public void testInvalidMaxAge() {
        String cookieString = "cookie1=value1; Max-Age=invalid";
        int maxAge = CookieUtils.getMaxAge(cookieString);

        assertEquals(-1, maxAge);
    }
}

