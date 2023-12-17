package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.GeneralDao;
import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import com.cba.core.wirememobile.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final GeneralDao dao;

    @Override
    public CrashTraceResponseDto create(CrashTraceRequestDto requestDto) throws Exception {
        return dao.create(requestDto);
    }
}
