package com.cba.core.wirememobile.controller;

import com.cba.core.wirememobile.controller.resource.RefreshTokenResource;
import com.cba.core.wirememobile.dto.TokenRefreshRequestDto;
import com.cba.core.wirememobile.dto.TokenRefreshResponseDto;
import com.cba.core.wirememobile.exception.TokenRefreshException;
import com.cba.core.wirememobile.model.TokenRefresh;
import com.cba.core.wirememobile.service.RefreshTokenService;
import com.cba.core.wirememobile.util.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Component
@RequiredArgsConstructor
@Validated
@Tag(name = "Refresh Token Management", description = "Provides Refresh Token Management API's")
public class RefreshTokenController implements RefreshTokenResource {

    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final JwtEncoder encoder;

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequestDto request) throws Exception {

        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(TokenRefresh::getUser)
                .map(user -> {
                    String token = jwtUtil.generateTokenFromUsername(user.getUserName(), encoder);
                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}

