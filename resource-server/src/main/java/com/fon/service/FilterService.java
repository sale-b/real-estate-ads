package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.dto.Exception.NoUserFoundForFilterSave;
import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.entity.User;
import com.fon.mapper.FilterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class FilterService {

    @Autowired
    FilterRepository filterRepository;
    @Autowired
    NotificationService notificationService;
    @Autowired
    FilterMapper filterMapper;
    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    public FilterDto save(Filter filter, String userEmail) {
        try {
            authorize(filter.getUser().getId(), userEmail);
            filter = filterRepository.save(filter);
            eventService.sendEvent(filter);
        } catch (InvalidDataAccessApiUsageException ex) {
            if (Objects.requireNonNull(ex.getMessage()).contains("com.fon.entity.Filter.user -> com.fon.entity.User")) {
                throw new NoUserFoundForFilterSave(ex.getMessage(), ex, filter);
            }
        }
        return filterMapper.toFilterDto(filter, notificationService);
    }

    public List<FilterDto> findByUserId(Long userId, String userEmail) {
        authorize(userId, userEmail);
        return filterMapper.toListDto(filterRepository.findByUserId(userId), notificationService);
    }

    public void deleteById(Long id, String userEmail) {
        authorize(filterRepository.findById(id).get().getUser().getId(), userEmail);
        filterRepository.deleteById(id);
    }

    private void authorize(Long userId, String userEmail) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && !user.get().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized!");
        }
    }
}
