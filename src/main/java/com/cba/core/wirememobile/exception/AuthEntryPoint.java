package com.cba.core.wirememobile.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class is used to handle all the JWT token level Exceptions globally.
 * This class is trigger though the spring security exception handling entrypoint added in thr security config class
 *
 * @see com.cba.core.wirememobile.security.SecurityConfig
 */
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        String message = "";
        if (((String) request.getAttribute("errorCode")).equals("BadCredentialsException")) {
            message = (((BadCredentialsException) request.getAttribute("errorObject")).getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        } else if (((String) request.getAttribute("errorCode")).equals("DeviceAuthException")) {
            message = (((DeviceAuthException) request.getAttribute("errorObject")).getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        } else if (((String) request.getAttribute("errorCode")).equals("AppSignAuthException")) {
            message = (((AppSignAuthException) request.getAttribute("errorObject")).getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        }

        try (PrintWriter writer = response.getWriter()) {

            writer.write("{\"error\": \"Custom error message: Access Denied with : " + message + "\"}");
        }
    }
}
