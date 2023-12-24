package de.gabriel.vertretungsplan.utils;

import java.util.HashMap;
import java.util.Map;

public class CookieUtils {

    public static Map<String, String> parseCookie(String cookieString) {
        Map<String, String> cookieMap = new HashMap<>();

        if (cookieString != null && !cookieString.isEmpty()) {
            String[] cookies = cookieString.split(";");
            for (String cookie : cookies) {
                String[] cookieParts = cookie.trim().split("=", 2);
                String key = cookieParts[0];
                String value = (cookieParts.length > 1) ? cookieParts[1] : "";

                // Remove quotes if the value is surrounded by them
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                cookieMap.put(key, value);
            }
        }

        return cookieMap;
    }

    public static String getCookieValue(String cookieString, String cookieName) {
        Map<String, String> cookieMap = parseCookie(cookieString);
        return cookieMap.get(cookieName);
    }

    public static boolean isHttpOnly(String cookieString) {
        Map<String, String> cookieMap = parseCookie(cookieString);
        return cookieMap.containsKey("HttpOnly");
    }

    public static String getPath(String cookieString) {
        Map<String, String> cookieMap = parseCookie(cookieString);
        return cookieMap.getOrDefault("Path", "/");
    }

    public static int getMaxAge(String cookieString) {
        Map<String, String> cookieMap = parseCookie(cookieString);
        String maxAgeString = cookieMap.get("Max-Age");
        if (maxAgeString != null) {
            try {
                return Integer.parseInt(maxAgeString);
            } catch (NumberFormatException ignore) {

            }
        }
        return -1; // Return -1 if the Max-Age is not specified or not a valid integer
    }
}
