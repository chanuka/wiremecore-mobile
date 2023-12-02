package com.cba.core.wirememobile.filter;

import com.cba.core.wirememobile.config.JwtConfig;
import com.cba.core.wirememobile.dto.UsernameAndPasswordAuthenticationRequestDto;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class UserNamePasswordVerifyFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtEncoder encoder;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            logger.debug("CustomUserNamePasswordFilter called--");

            UsernameAndPasswordAuthenticationRequestDto authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequestDto.class);

//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(),
//                    authenticationRequest.getPassword()
//            );
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            authRequest.setDetails(authenticationRequest);


            Authentication authenticate = authenticationManager.authenticate(authRequest);
            return authenticate;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UsernameAndPasswordAuthenticationRequestDto userDto = (UsernameAndPasswordAuthenticationRequestDto) authResult.getDetails();
        System.out.println("deviceSerial:" + customUserDetailsService.validateUserDevice(userDto));

        String token = jwtUtil.generateTokenFromAuthResult(authResult, encoder);
        TokenRefresh refreshToken = refreshTokenService.createRefreshToken(authResult.getName());

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        response.addHeader("Refresh_Token", "" + refreshToken.getToken());
    }

}
