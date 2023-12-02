package com.cba.core.wirememobile.dao.impl;

import com.cba.core.wirememobile.dao.ResourceDao;
import com.cba.core.wirememobile.dto.ResourceResponseDto;
import com.cba.core.wirememobile.exception.NotFoundException;
import com.cba.core.wirememobile.mapper.ResourceMapper;
import com.cba.core.wirememobile.model.Resource;
import com.cba.core.wirememobile.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class ResourceDaoImpl implements ResourceDao {

    private final ResourceRepository repository;

    @Override
    public List<ResourceResponseDto> findAll() throws Exception {
        List<Resource> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Resources found");
        }
        return entityList
                .stream()
                .map(ResourceMapper::toDto)
                .collect(Collectors.toList());
    }
}
