package com.cba.core.wirememobile.controller.resource;

import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
public interface GeneralResource {

    @PostMapping("/general/crash-log")
    ResponseEntity<CrashTraceResponseDto> setCrashReport(@Valid @RequestBody CrashTraceRequestDto requestDto) throws Exception;
}
