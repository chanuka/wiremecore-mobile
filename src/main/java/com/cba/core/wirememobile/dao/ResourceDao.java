package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.dto.ResourceResponseDto;

import java.util.List;

public interface ResourceDao {
    List<ResourceResponseDto> findAll() throws Exception;
}
