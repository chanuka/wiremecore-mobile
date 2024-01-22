package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.PermissionDao;
import com.cba.core.wirememobile.dto.PermissionResponseDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.PermissionMapper;
import com.cba.core.wirememobile.model.Permission;
import com.cba.core.wirememobile.repository.GlobalAuditEntryRepository;
import com.cba.core.wirememobile.service.PermissionService;
import com.cba.core.wirememobile.util.UserBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionDao dao;
//    private final GlobalAuditEntryRepository globalAuditEntryRepository;
//    private final ObjectMapper objectMapper;
//    private final UserBeanUtil userBeanUtil;

//    @Value("${application.resource.permissions}")
//    private String resource;

    @Override
    public Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception {
        Page<Permission> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Permissions found");
        }
        return entitiesPage.map(PermissionMapper::toDto);
    }

    @Override
    public List<PermissionResponseDto> findAll() throws Exception {
        return dao.findAll().stream().map(PermissionMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<PermissionResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public PermissionResponseDto findById(int id) throws Exception {
        Permission entity = dao.findById(id);
        return PermissionMapper.toDto(entity);
    }


    @Override
    public List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException {
        Iterable<Permission> irt = dao.findAllPermissionsByUser(username);
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(PermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }
}
