package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.entity.User;
import com.fon.entity.enumeration.EventAction;
import com.fon.mapper.FilterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FilterService {

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    FilterMapper filterMapper;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;



    public Filter save(Filter filter, String userEmail) {
        authorize(filter.getUser().getId(), userEmail);
        return filterRepository.save(filter);
    }

    public List<FilterDto> findByUserIdOrderByIdAsc(Long userId, String userEmail) {
        authorize(userId, userEmail);
        return filterMapper.toListDto(filterRepository.findByUserIdOrderByIdAsc(userId), notificationService);
    }

    public void deleteById(Long id, String userEmail) {
        Filter filter = filterRepository.findById(id).get();
        authorize(filter.getUser().getId(), userEmail);
        filterRepository.deleteById(id);
    }

    private void authorize(Long userId, String userEmail) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && !user.get().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized!");
        }
    }
}
