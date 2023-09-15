package com.fon.DAO;

import com.fon.entity.FilterEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FilterEventRepository extends JpaRepository<FilterEvent, Long> {
    List<FilterEvent> findBySentFalseAndCreatedOnBeforeOrderByIdAsc(LocalDateTime thresholdTime);
}