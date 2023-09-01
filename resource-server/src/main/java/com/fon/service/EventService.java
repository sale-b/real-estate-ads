package com.fon.service;


import com.fon.entity.BaseEntity;
import com.fon.entity.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class EventService {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private NotificationService notificationService;

    public void sendRealEstateEvent(BaseEntity entity) {
        jmsTemplate.convertAndSend("real-estate-events.que", entity);
    }

    public void sendDeleteEvent(BaseEntity entity) {
        jmsTemplate.convertAndSend("entity-removals.que", entity);
    }

    @JmsListener(destination = "notifications.topic", containerFactory = "topicConnectionFactory")
    public void receiveNotification(Notification notification) {
        log.info("Got notification {}", notification);
        notificationService.save(notification);
    }

}
