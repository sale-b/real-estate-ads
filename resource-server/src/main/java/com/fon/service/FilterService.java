package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.entity.User;
import com.fon.mapper.FilterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        authorize(filter.getUser().getId(), userEmail);
        filter = filterRepository.save(filter);
        eventService.sendRealEstateEvent(filter);
        return filterMapper.toFilterDto(filter, notificationService);
    }

    public List<FilterDto> findByUserId(Long userId, String userEmail) {
        authorize(userId, userEmail);
        return filterMapper.toListDto(filterRepository.findByUserId(userId), notificationService);
    }

    public void deleteById(Long id, String userEmail) {
        authorize(filterRepository.findById(id).get().getUser().getId(), userEmail);
        filterRepository.deleteById(id);
        eventService.sendDeleteEvent(Filter.builder().id(id).build());
    }

    private void authorize(Long userId, String userEmail) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && !user.get().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized!");
        }
    }
}
