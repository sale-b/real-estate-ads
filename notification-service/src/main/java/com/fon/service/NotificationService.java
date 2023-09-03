package com.fon.service;

import com.fon.DAO.NotificationRepository;
import com.fon.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<Notification> findAllByRealEstateId(Long realEstateId) {
        return notificationRepository.findAllByRealEstateId(realEstateId);
    }

    public void deleteAll(List<Notification> notifications) {
        notificationRepository.deleteAll(notifications);
    }
}
