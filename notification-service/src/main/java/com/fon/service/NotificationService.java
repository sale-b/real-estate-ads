package com.fon.service;

import com.fon.DAO.NotificationRepository;
import com.fon.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Notification notification) {
        jmsTemplate.convertAndSend("notifications.topic", notification);
    }

    public Notification save(Notification notification){
        return notificationRepository.save(notification);
    }
}
