package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.GeneralDao;
import com.cba.core.wirememobile.dto.CrashTraceRequestDto;
import com.cba.core.wirememobile.dto.CrashTraceResponseDto;
import com.cba.core.wirememobile.dto.TransactionResponseDto;
import com.cba.core.wirememobile.mapper.CrashTraceMapper;
import com.cba.core.wirememobile.mapper.TransactionMapper;
import com.cba.core.wirememobile.model.CrashTrace;
import com.cba.core.wirememobile.model.TransactionCore;
import com.cba.core.wirememobile.repository.CrashTraceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class GeneralDaoImpl implements GeneralDao {

    private final CrashTraceRepository repository;

    @Override
    public CrashTraceResponseDto create(CrashTraceRequestDto requestDto) throws Exception {

        CrashTrace toInsert = CrashTraceMapper.toModel(requestDto);
        CrashTrace savedEntity = repository.save(toInsert);
        CrashTraceResponseDto responseDto = CrashTraceMapper.toDto(savedEntity);

//        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
//                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
//                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }
}
