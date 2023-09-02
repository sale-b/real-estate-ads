package com.fon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendNotificationToUser(Map<String, Object> notification) {
        String message = "newNotificationReceived";
        messagingTemplate.convertAndSendToUser(((Map) notification.get("filter")).get("userId").toString(), "/queue/notifications", message);
    }

    @JmsListener(destination = "notifications.topic")
    public void receiveNotification(Map<String, Object> notification) {
        log.info("Got notification {}", notification);
        sendNotificationToUser(notification);
    }
}
