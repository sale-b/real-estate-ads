package com.fon.config;

import com.fon.DAO.FilterRepository;
import com.fon.DAO.UserRepository;
import com.fon.dto.Exception.NoUserFoundForFilterSave;
import com.fon.entity.User;
import com.fon.mapper.FilterMapper;
import com.fon.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private NotificationService notificationService;

    @ExceptionHandler(NoUserFoundForFilterSave.class)
    @Transactional
    protected ResponseEntity<Object> handleNoUserFoundForFilterSave(NoUserFoundForFilterSave ex) {
        //TODO
        userRepository.save(User.builder().id(ex.getFilter().getUser().getId()).email("default@default.com").build());
        return ResponseEntity.status(HttpStatus.CREATED).body(FilterMapper.INSTANCE.toFilterDto(filterRepository.save(ex.getFilter()), notificationService));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}