package com.fon.DAO;

import com.fon.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByRealEstateId(Long realEstateId);
    List<Notification> findBySentFalseAndCreatedOnBefore(LocalDateTime thresholdTime);
}