package com.fon.service;


import com.fon.DAO.FilterEventRepository;
import com.fon.DAO.FilterRepository;
import com.fon.DAO.RealEstateEventRepository;
import com.fon.DAO.RealEstateRepository;
import com.fon.entity.*;
import com.fon.entity.enumeration.EventAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    @Autowired
    private RealEstateRepository realEstateRepository;
    @Autowired
    private FilterRepository filterRepository;

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
            if (event instanceof RealEstateEvent) {
                RealEstateEvent realEstateEvent = (RealEstateEvent) event;
                realEstateRepository.findById(realEstateEvent.getRealEstateId()).ifPresentOrElse(re -> {
                            realEstateEvent.setRealEstate(re);
                            sendEvent(realEstateEvent);
                        },
                        () -> {
                            if (event.getEventAction().equals(EventAction.DELETE)) {
                                realEstateEvent.setRealEstate(RealEstate.builder().id(realEstateEvent.getRealEstateId()).build());
                                sendEvent(realEstateEvent);
                            } else {
                                realEstateEventRepository.delete(realEstateEvent);
                            }
                        }
                );

            } else if (event instanceof FilterEvent) {
                FilterEvent filterEvent = (FilterEvent) event;
                filterRepository.findById(filterEvent.getFilterId()).ifPresentOrElse(f -> {
                            filterEvent.setFilter(f);
                            sendEvent(filterEvent);
                        },
                        () -> {
                            if (event.getEventAction().equals(EventAction.DELETE)) {
                                filterEvent.setFilter(Filter.builder().id(filterEvent.getFilterId()).build());
                                sendEvent(filterEvent);
                            } else {
                                filterEventRepository.delete(filterEvent);
                            }
                        }
                );
            }
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
