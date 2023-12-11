package com.cba.core.wirememobile.filter;

import com.cba.core.wirememobile.config.JwtConfig;
import com.cba.core.wirememobile.dto.SuccessAuthResponse;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
import com.cba.core.wirememobile.exception.AppSignAuthException;
import com.cba.core.wirememobile.exception.DeviceAuthException;
import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.service.CustomUserDetailsService;
import com.cba.core.wirememobile.service.RefreshTokenService;
import com.cba.core.wirememobile.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class UserNamePasswordVerifyFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtEncoder encoder;
    private final ObjectMapper objectMapper;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            logger.debug("CustomUserNamePasswordFilter called--");

            UsernameAndPasswordAuthenticationRequestDto authenticationRequest = null;
            authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequestDto.class);


//          Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword());
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            authRequest.setDetails(authenticationRequest); // set user object for future usage - optional

            /*
            All the login attempts should be logged , save to database
             */
            Authentication authenticate = authenticationManager.authenticate(authRequest);

            return authenticate;

        } catch (BadCredentialsException e) {
            request.setAttribute("errorObject", e);
            request.setAttribute("errorCode", "BadCredentialsException");
            logger.error(e.getMessage());
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        try {

            /*
             * if the user credentials are validated only, below validation will be processed
             */
            SuccessAuthResponse successAuthResponse = customUserDetailsService.validateUserDevice((UsernameAndPasswordAuthenticationRequestDto) authResult.getDetails());


            String token = jwtUtil.generateTokenFromAuthResult(authResult, encoder);
            TokenRefresh refreshToken = refreshTokenService.createRefreshToken(authResult.getName());

            response.setContentType("application/json");
            successAuthResponse.setToken(token);
            successAuthResponse.setRefreshToken(refreshToken.getToken());
            try (PrintWriter writer = response.getWriter()) {
                writer.println(objectMapper.writeValueAsString(successAuthResponse));
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (DeviceAuthException de) {
            request.setAttribute("errorObject", de);
            request.setAttribute("errorCode", "DeviceAuthException");
            logger.error(de.getMessage());
            throw de;
        } catch (AppSignAuthException ae) {
            request.setAttribute("errorObject", ae);
            request.setAttribute("errorCode", "AppSignAuthException");
            logger.error(ae.getMessage());
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
