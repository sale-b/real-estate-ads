package com.fon.service;


import com.fon.DAO.FilterEventRepository;
import com.fon.DAO.RealEstateEventRepository;
import com.fon.entity.*;
import com.fon.entity.enumeration.EventAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EventService {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RealEstateEventRepository realEstateEventRepository;
    @Autowired
    private FilterEventRepository filterEventRepository;

    public void sendEvent(BaseEvent event) {
        jmsTemplate.convertAndSend("real-estate-events.que", event);
        event.setSent(true);
        if (event instanceof RealEstateEvent) {
            realEstateEventRepository.save((RealEstateEvent) event);
        } else if (event instanceof FilterEvent) {
            filterEventRepository.save((FilterEvent) event);
        }
    }

    @Scheduled(fixedRate = 15000)
    public void sendFailed() {
        //created more than 1 min ago and not sent
        LocalDateTime thresholdTime = LocalDateTime.now().minus(1, ChronoUnit.MINUTES);
        List<BaseEvent> failedEvents = new ArrayList<>();
        failedEvents.addAll(realEstateEventRepository.findBySentFalseAndCreatedOnBeforeOrderByIdAsc(thresholdTime));
        failedEvents.addAll(filterEventRepository.findBySentFalseAndCreatedOnBeforeOrderByIdAsc(thresholdTime));
        failedEvents.stream().forEach(event -> {
            sendEvent(event);
        });
    }

    @JmsListener(destination = "notifications.topic", containerFactory = "topicConnectionFactory")
    public void receiveNotification(Notification notification) {
        log.info("Got notification {}", notification);
        notificationService.save(notification);
    }

    public BaseEvent createEvent(RealEstate realEstate, EventAction eventAction) {
        RealEstateEvent event = RealEstateEvent.builder()
                .realEstateId(realEstate.getId())
                .realEstate(realEstate)
                .eventAction(eventAction)
                .sent(false)
                .build();
        return realEstateEventRepository.save(event);
    }

    public BaseEvent createEvent(Filter filter, EventAction eventAction) {
        FilterEvent event = FilterEvent.builder()
                .filterId(filter.getId())
                .filter(filter)
                .eventAction(eventAction)
                .sent(false)
                .build();
        return filterEventRepository.save(event);
    }
}
