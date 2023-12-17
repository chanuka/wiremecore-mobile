package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;

public interface GeneralDao {

    CrashTraceResponseDto create(CrashTraceRequestDto requestDto) throws Exception;
}
