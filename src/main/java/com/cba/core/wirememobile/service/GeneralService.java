package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;

public interface GeneralService {

    CrashTraceResponseDto create(CrashTraceRequestDto requestDto) throws Exception;

}
