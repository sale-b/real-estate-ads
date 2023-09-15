package com.fon.service;

import com.fon.config.DestinationProperties;
import com.fon.entity.Filter;
import com.fon.entity.Notification;
import com.fon.entity.RealEstate;
import com.fon.message.BaseEvent;
import com.fon.message.FilterEvent;
import com.fon.message.RealEstateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableConfigurationProperties(DestinationProperties.class)
@Slf4j
@Transactional
public class EventProcessorService {

    @Autowired
    RealEstateService realEstateService;
    @Autowired
    FilterService filterService;
    @Autowired
    EmailService emailService;
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    NotificationService notificationService;

    @JmsListener(destination = "${destination.events}", containerFactory = "queueConnectionFactory")
    public void receiveRealEstateEvent(BaseEvent event) {
        log.info("Got event {}", event);
        if (event instanceof FilterEvent) {
            FilterEvent filterEvent = (FilterEvent) event;
            switch (event.getEventAction()) {
                case UPDATE:
                    filterEvent.getFilter().setModifiedOn(LocalDateTime.now());
                    Filter filter = filterService.save(filterEvent.getFilter());
                    List<Notification> notifications = generateNotifications(filter);
                    sendNotifications(notifications);
                    break;
                case DELETE:
                    log.info("Got to delete {}", event);
                    filterService.delete(((FilterEvent) event).getFilter());
                    break;
            }

        } else if (event instanceof RealEstateEvent) {
            RealEstateEvent realEstateEvent = (RealEstateEvent) event;
            switch (event.getEventAction()) {
                case UPDATE:
                    realEstateEvent.getRealEstate().setModifiedOn(LocalDateTime.now());
                    RealEstate realEstate = realEstateService.save(realEstateEvent.getRealEstate());
                    List<Notification> notifications = generateNotifications(realEstate);
                    sendNotifications(notifications);
                    break;
                case DELETE:
                    log.info("Got to delete {}", event);
                    realEstateService.delete(((RealEstateEvent) event).getRealEstate());
                    break;
            }
        }
    }

    private List<Notification> generateNotifications(Filter filter) {
        List<RealEstate> realEstateList = realEstateService.getRealEstates(filter);
        List<Notification> notifications = new ArrayList<>();
        for (RealEstate realEstate : realEstateList) {
            log.info("FOUND REALESTATE {}", realEstate);
            notifications.add(generateNotification(filter, realEstate));
        }
        return notifications;
    }

    private List<Notification> generateNotifications(RealEstate realEstate) {
        List<Filter> filters = filterService.getFilters(realEstate);
        List<Notification> notifications = new ArrayList<>();
        for (Filter filter : filters) {
            log.info("FOUND FILTER {}", filter);
            notifications.add(generateNotification(filter, realEstate));
        }
        return notifications;
    }

    private Notification generateNotification(Filter filter, RealEstate realEstate) {
        LocalDateTime createdOn = LocalDateTime.now();
        Notification notification = Notification.builder()
                .filter(filter)
                .filterId(filter.getId())
                .realEstate(realEstate)
                .seen(false)
                .sent(false)
                .createdOn(createdOn)
                .modifiedOn(createdOn)
                .build();
        return notificationService.save(notification);
    }


    private void sendNotifications(List<Notification> notifications) {
        notifications.parallelStream().forEach(notification -> {
            if (notification.getFilter().getSubscribed()) {
                emailService.sendEmail(notification);
            }
            notificationService.sendNotification(notification);
        });
    }
}
