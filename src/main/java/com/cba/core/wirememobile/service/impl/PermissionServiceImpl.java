package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.PermissionDao;
import com.cba.core.wirememobile.dto.PermissionResponseDto;
import com.cba.core.wirememobile.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionDao dao;

    @Override
    public Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception {
        return dao.findAll(page, pageSize);
    }

    @Override
    public List<PermissionResponseDto> findAll() throws Exception {
        return dao.findAllPermissionsByUser();
    }

    @Override
    public Page<PermissionResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public PermissionResponseDto findById(int id) throws Exception {
        return dao.findById(id);
    }


    @Override
    public List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException {
        return dao.findAllPermissionsByUser(username);
    }
}
