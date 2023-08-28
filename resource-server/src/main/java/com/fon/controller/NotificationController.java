package com.fon.controller;

import com.fon.dto.NotificationDto;
import com.fon.dto.NotificationsRequestDto;
import com.fon.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/page")
    public ResponseEntity<Page<NotificationDto>> getNotifications(
            @RequestBody NotificationsRequestDto notificationsRequestDto) {
        System.out.println(notificationsRequestDto);
        Page<NotificationDto> notifications = notificationService.getNotifications(notificationsRequestDto);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Long> countUnseenNotifications(@PathVariable Long userId){
        return ResponseEntity.ok(notificationService.countByUserIdAndSeenFalse(userId));
    }

    @PutMapping("/seen/{notificationId}")
    public ResponseEntity<Long> markNotificationAsSeen(@PathVariable Long notificationId){
        notificationService.markNotificationAsSeen(notificationId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/seen/filter/{filterId}")
    public ResponseEntity<Long> markAllNotificationsAsSeenForFilter(@PathVariable Long filterId){
        notificationService.markAllNotificationsAsSeenForFilter(filterId);
        return ResponseEntity.ok(null);
    }

}
