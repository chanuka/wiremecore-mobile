package com.cba.core.wirememobile.mapper;

import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import com.cba.core.wirememobile.model.CrashTrace;

public class CrashTraceMapper {

    public static CrashTraceResponseDto toDto(CrashTrace trace) {
        CrashTraceResponseDto responseDto = new CrashTraceResponseDto();
        responseDto.setId(trace.getId());
        responseDto.setTime(trace.getTime());
        responseDto.setTrace(trace.getTrace());
        return responseDto;
    }

    public static CrashTrace toModel(CrashTraceRequestDto responseDto) {
        CrashTrace entity = new CrashTrace();
        entity.setTime(responseDto.getTime());
        entity.setTrace(responseDto.getTrace());
        return entity;
    }
}
