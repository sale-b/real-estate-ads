package com.fon.service;

import com.fon.config.DestinationProperties;
import com.fon.entity.BaseEntity;
import com.fon.entity.Filter;
import com.fon.entity.Notification;
import com.fon.entity.RealEstate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableConfigurationProperties(DestinationProperties.class)
@Slf4j
public class EventProcessorService {

    @Autowired
    RealEstateService realEstateService;
    @Autowired
    FilterService filterService;
    @Autowired
    EmailService emailService;

    @Autowired
    NotificationService notificationService;

    @JmsListener(destination = "${destination.events}", containerFactory = "queueConnectionFactory")
    public void receiveSaveEvent(BaseEntity event) {
        if (event instanceof Filter) {
            Filter filter = (Filter) event;
            log.info("Got for save {}", filter);
            filter = filterService.save(filter);
            sendNotification(filter);
        } else if (event instanceof RealEstate) {
            RealEstate realEstate = (RealEstate) event;
            log.info("Got for save {}", realEstate);
            realEstate.setCreatedOn(LocalDateTime.now());
            realEstate = realEstateService.save(realEstate);
            sendNotification(realEstate);
        }
    }

    @JmsListener(destination = "${destination.removals}", containerFactory = "queueConnectionFactory")
    @Transactional
    public void receiveDeleteEvent(BaseEntity event) throws Exception {
        if (event instanceof Filter) {
            log.info("Got to delete {}", event);
            filterService.delete((Filter) event);
        } else if (event instanceof RealEstate) {
            log.info("Got to delete {}", event);
            realEstateService.delete((RealEstate) event);
        }
    }

    private void sendNotification(Filter filter) {
        log.info("FOUND FILTER {}", filter);
        List<RealEstate> realEstateList = realEstateService.getRealEstates(filter);
        for (RealEstate realEstate : realEstateList) {
            sendNotification(filter, realEstate);
        }
    }

    private void sendNotification(RealEstate realEstate) {
        log.info("FOUND REALESTATE {}", realEstate);
        List<Filter> filters = filterService.getFilters(realEstate);
        for (Filter filter : filters) {
            sendNotification(filter, realEstate);
        }
    }

    private void sendNotification(Filter filter, RealEstate realEstate) {
        if (filter.getSubscribed()) {
            emailService.sendEmail(realEstate.toString(), filter.getUserEmail());
        }
        Notification notification = Notification.builder()
                .filter(filter)
                .filterId(filter.getId())
                .realEstate(realEstate)
                .seen(false)
                .createdOn(LocalDateTime.now())
                .build();
        notification = notificationService.save(notification);
        notificationService.sendNotification(notification);
    }
}
