package com.fon.DAO;

import com.fon.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findAll(Specification<Notification> spec, Pageable pageable);

    long countByFilterIdAndSeenFalse(Long filterId);
    List<Notification> findBySeenFalse();


    @Query("SELECT COUNT(n.id) FROM Notification n LEFT JOIN n.filter f WHERE f.user.id = :userId AND n.seen = false")
    long countUnseenNotificationsByUserId(Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.seen = true, n.modifiedOn = :modifiedOn WHERE n.id = :notificationId")
    void markNotificationAsSeen(@Param("notificationId") Long notificationId,@Param("modifiedOn") LocalDateTime modifiedOn);

    @Modifying
    @Query("UPDATE Notification n SET n.seen = true, n.modifiedOn = :modifiedOn WHERE n.filter.id = :filterId")
    void markAllNotificationsAsSeenForFilter(@Param("filterId") Long filterId, @Param("modifiedOn") LocalDateTime modifiedOn);

    List<Notification> findAllByRealEstateId(Long realEstateId);

}