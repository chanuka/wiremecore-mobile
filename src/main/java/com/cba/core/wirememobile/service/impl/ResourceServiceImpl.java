package com.cba.core.wirememobile.service.impl;

import com.cba.core.wirememobile.dao.ResourceDao;
import com.cba.core.wirememobile.dto.ResourceResponseDto;
import com.cba.core.wirememobile.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceDao dao;

    @Override
    public List<ResourceResponseDto> findAll() throws Exception {
        return dao.findAll();
    }
}
