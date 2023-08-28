package com.fon.controller;

import com.fon.dto.NotificationDto;
import com.fon.dto.NotificationsRequestDto;
import com.fon.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notification")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/page")
    public ResponseEntity<Page<NotificationDto>> getNotifications(
            @RequestBody NotificationsRequestDto notificationsRequestDto, Principal principal) {
        System.out.println(notificationsRequestDto);
        Page<NotificationDto> notifications = notificationService.getNotifications(notificationsRequestDto, principal.getName());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Long>> countUnseenNotifications(@PathVariable Long userId, Principal principal) {
        Map<String, Long> responseMap = new HashMap<>();
        responseMap.put("count", notificationService.countByUserIdAndSeenFalse(userId, principal.getName()));
        return ResponseEntity.ok(responseMap);
    }

    @PutMapping("/seen/{notificationId}")
    public ResponseEntity<Long> markNotificationAsSeen(@PathVariable Long notificationId, Principal principal) {
        notificationService.markNotificationAsSeen(notificationId, principal.getName());
        return ResponseEntity.ok(null);
    }

    @PutMapping("/seen/filter/{filterId}")
    public ResponseEntity<Long> markAllNotificationsAsSeenForFilter(@PathVariable Long filterId, Principal principal) {
        notificationService.markAllNotificationsAsSeenForFilter(filterId, principal.getName());
        return ResponseEntity.ok(null);
    }

}
