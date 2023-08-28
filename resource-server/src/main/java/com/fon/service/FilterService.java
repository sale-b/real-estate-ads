package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.dto.Exception.NoUserFoundForFilterSave;
import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.mapper.FilterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class FilterService {

    @Autowired
    FilterRepository filterRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    FilterMapper filterMapper;

    public FilterDto save(Filter filter) {
        try {
            filter = filterRepository.save(filter);
        } catch (InvalidDataAccessApiUsageException ex) {
            if (Objects.requireNonNull(ex.getMessage()).contains("com.fon.entity.Filter.user -> com.fon.entity.User")) {
                throw new NoUserFoundForFilterSave(ex.getMessage(), ex, filter);
            }
        }
        return filterMapper.toFilterDto(filter, notificationService);
    }

    public List<FilterDto> findByUserId(Long userId) {
        return filterMapper.toListDto(filterRepository.findByUserId(userId),notificationService);
    }

    public void deleteById(Long id) {
        filterRepository.deleteById(id);
    }
}
