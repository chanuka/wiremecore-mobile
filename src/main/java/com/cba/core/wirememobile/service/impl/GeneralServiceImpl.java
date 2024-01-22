package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.GeneralDao;
import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import com.cba.core.wirememobile.mapper.CrashTraceMapper;
import com.cba.core.wirememobile.model.CrashTrace;
import com.cba.core.wirememobile.service.GeneralService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {

    private final GeneralDao dao;

    @Override
    public CrashTraceResponseDto create(CrashTraceRequestDto requestDto) throws Exception {
        CrashTrace toInsert = CrashTraceMapper.toModel(requestDto);
        CrashTrace savedEntity = dao.create(toInsert);
        CrashTraceResponseDto responseDto = CrashTraceMapper.toDto(savedEntity);

//        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
//                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
//                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }
}
