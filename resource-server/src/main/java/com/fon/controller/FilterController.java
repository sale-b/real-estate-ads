package com.fon.controller;

import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.entity.enumeration.EventAction;
import com.fon.mapper.FilterMapper;
import com.fon.service.EventService;
import com.fon.service.FilterService;
import com.fon.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/filter")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class FilterController {

    @Autowired
    FilterService filterService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    EventService eventService;

    @Autowired
    FilterMapper filterMapper;

    @PostMapping()
    public ResponseEntity<FilterDto> save(@RequestBody FilterDto filterDto, Principal principal) {
        Filter filter = FilterMapper.INSTANCE.toFilter(filterDto);
        filter = filterService.save(filter, principal.getName());
        filter.getUser().setEmail(principal.getName());
        try {
            Filter finalFilter = filter;
            Runnable runnable = () -> {
                eventService.sendEvent(eventService.createEvent(finalFilter, EventAction.UPDATE));
            };
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(filterMapper.toFilterDto(filter, notificationService));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FilterDto>> findByUserId(@PathVariable Long userId, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(filterService.findByUserIdOrderByIdAsc(userId, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id, Principal principal) {
        filterService.deleteById(id, principal.getName());
        try {
            Runnable runnable = () -> {
                eventService.sendEvent(eventService.createEvent(Filter.builder().id(id).build(), EventAction.DELETE));
            };
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


}
