package com.cba.core.wirememobile.controller.resource;

import com.cba.core.wirememobile.dto.ChangePasswordRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public interface UserResource {

    @PostMapping("/changePassword")
    ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) throws Exception;

    @PostMapping("/otps")
    ResponseEntity<String> ValidateOTP(@Valid @RequestBody String otp) throws Exception;
}
