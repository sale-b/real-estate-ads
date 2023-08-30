package com.fon.service;


import com.fon.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class EventService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendRealEstateEvent(BaseEntity entity) {
        jmsTemplate.convertAndSend("real-estate-events", entity);
    }
    public void sendDeleteEvent(BaseEntity entity) {
        jmsTemplate.convertAndSend("entity-removals", entity);
    }

}
