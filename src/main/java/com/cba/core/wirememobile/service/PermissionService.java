package com.cba.core.wirememobile.service;

import com.cba.core.wirememobile.dto.PermissionResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface PermissionService {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws Exception;

    Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception;

    List<PermissionResponseDto> findAll() throws Exception;

    Page<PermissionResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception;

    PermissionResponseDto findById(int id) throws Exception;

}
