package com.fon.service;

import com.fon.config.DestinationProperties;
import com.fon.entity.BaseEntity;
import com.fon.entity.Filter;
import com.fon.entity.RealEstate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @JmsListener(destination = "${destination.events}")
    public void receiveSaveEvent(BaseEntity event) {
        if (event instanceof Filter) {
            Filter filter = (Filter) event;
            log.info("Got for save {}", filter);
            filterService.save(filter);
            sendNotifications(filter);
        } else if (event instanceof RealEstate) {
            RealEstate realEstate = (RealEstate) event;
            log.info("Got for save {}", realEstate);
            realEstateService.save(realEstate);
            sendNotifications(realEstate);
        }
    }

    @JmsListener(destination = "${destination.removals}")
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

    private void sendNotifications(Filter filter) {
        List<RealEstate> realEstateList = realEstateService.getRealEstates(filter);
        for (RealEstate realEstate : realEstateList) {
            log.info("FOUND REALESTATE {}", realEstate);
            if (filter.getSubscribed()) {
                emailService.sendEmail(realEstate.toString(), filter.getUserEmail());
            }

        }
    }

    private void sendNotifications(RealEstate realEstate) {
        List<Filter> filters = filterService.getFilters(realEstate);
        for (Filter filter : filters) {
            log.info("FOUND FILTER {}", filter);
            if (filter.getSubscribed()) {
                emailService.sendEmail(realEstate.toString(), filter.getUserEmail());
            }
        }
    }
}
