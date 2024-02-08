package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.PermissionDao;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.model.Permission;
import com.cba.core.wirememobile.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PermissionDaoImpl implements PermissionDao {

    private final PermissionRepository repository;

    @Override
    @Cacheable("permissions")
    public Page<Permission> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("permissions")
    public List<Permission> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Permission findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
    }

    @Override
//    @Cacheable("permissions")
    public List<Permission> findAllPermissionsByUser(String username) throws SQLException {
        return repository.findAllPermissionsByUser(username);
    }

}
