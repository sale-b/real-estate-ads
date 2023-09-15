package com.fon.service;

import com.fon.DAO.NotificationRepository;
import com.fon.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(Notification notification) {
        jmsTemplate.convertAndSend("notifications.topic", notification);
        notification.setModifiedOn(LocalDateTime.now());
        notification.setSent(true);
        save(notification);
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

    @Scheduled(fixedRate = 15000)
    public void sendFailed(){
        //created more than 1 min ago and not sent
        LocalDateTime thresholdTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        notificationRepository.findBySentFalseAndCreatedOnBefore(thresholdTime).stream().forEach(notification -> {
            sendNotification(notification);
        });
    }
}
