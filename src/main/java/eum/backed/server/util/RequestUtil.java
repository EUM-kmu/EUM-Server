package eum.backed.server.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
@Slf4j
public class RequestUtil {

    public static String getAuthorizationToken(String header) {
        header.replace("Bearer ", "");
        // Authorization: Bearer <access_token>
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        // Remove Bearer from string
        String[] parts = header.split(" ");
//        if (parts.length != 2) {
//            throw new IllegalArgumentException("Invalid authorization header");
//        }
        // Get token
        log.warn(parts[1]);
        return parts[1];
    }

    public static String getAuthorizationToken(HttpServletRequest request) {
        return getAuthorizationToken(request.getHeader("Authorization"));
    }
}