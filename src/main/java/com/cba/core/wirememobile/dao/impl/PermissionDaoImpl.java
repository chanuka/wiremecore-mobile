package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.PermissionDao;
import com.cba.core.wirememobile.dto.PermissionResponseDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.PermissionMapper;
import com.cba.core.wirememobile.model.Permission;
import com.cba.core.wirememobile.repository.GlobalAuditEntryRepository;
import com.cba.core.wirememobile.repository.PermissionRepository;
import com.cba.core.wirememobile.util.UserBeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Transactional
@RequiredArgsConstructor
public class PermissionDaoImpl implements PermissionDao {

    private final PermissionRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.permissions}")
    private String resource;

    @Override
    @Cacheable("permissions")
    public Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Permission> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Permissions found");
        }
        return entitiesPage.map(PermissionMapper::toDto);
    }

    @Override
    public PermissionResponseDto findById(int id) throws Exception {
        Permission entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
        return PermissionMapper.toDto(entity);
    }


    @Override
//    @Cacheable("permissions")
    public List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException {
        Iterable<Permission> irt = repository.findAllPermissionsByUser(username);
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(PermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<PermissionResponseDto> findAllPermissionsByUser() throws SQLException {
        Iterable<Permission> irt = repository.findAllPermissionsByUser(userBeanUtil.getUsername());
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(PermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }
}
