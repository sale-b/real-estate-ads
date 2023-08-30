package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.DAO.RealEstateRepository;
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
    FilterRepository filterRepository;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RealEstateService realEstateService;

    @JmsListener(destination = "${destination.events}")
    public void receiveSaveEvent(BaseEntity event) {
        if (event instanceof Filter) {
            Filter filter = (Filter) event;
            log.info("Got for save {}", filter);
            filterRepository.save(filter);
            List<RealEstate> realEstateList = realEstateService.getRealEstates(filter);
            for (RealEstate realEstate : realEstateList){
                log.info("FOUND REALESTATE {}", realEstate);
            }
        } else if (event instanceof RealEstate) {
            log.info("Got for save {}", event);
            realEstateRepository.save((RealEstate) event);
        }
    }

    @JmsListener(destination = "${destination.removals}")
    @Transactional
    public void receiveDeleteEvent(BaseEntity event) throws Exception {
        if (event instanceof Filter) {
            log.info("Got to delete {}", event);
            filterRepository.delete((Filter) event);
        } else if (event instanceof RealEstate) {
            log.info("Got to delete {}", event);
            realEstateRepository.delete((RealEstate) event);
        }
    }

}
