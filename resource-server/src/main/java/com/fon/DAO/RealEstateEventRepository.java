package com.fon.DAO;

import com.fon.entity.RealEstateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RealEstateEventRepository extends JpaRepository<RealEstateEvent, Long> {
    List<RealEstateEvent> findBySentFalseAndCreatedOnBeforeOrderByIdAsc(LocalDateTime thresholdTime);

}