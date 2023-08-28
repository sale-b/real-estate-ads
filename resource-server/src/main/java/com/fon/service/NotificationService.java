package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.DAO.NotificationRepository;
import com.fon.dto.NotificationDto;
import com.fon.dto.NotificationsRequestDto;
import com.fon.entity.Notification;
import com.fon.entity.User;
import com.fon.mapper.NotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    UserService userService;

    @Autowired
    FilterRepository filterRepository;

    private Set<Notification> sentNotifications = new HashSet<>();

    public Page<NotificationDto> getNotifications(NotificationsRequestDto notificationsRequestDto, String userEmail) {

        authorize(filterRepository.findById(notificationsRequestDto.getFilters().getId()).get().getUser().getId(), userEmail);

        Specification<Notification> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = buildCriteria(notificationsRequestDto, root, criteriaBuilder);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");
        Pageable pageable = PageRequest.of(notificationsRequestDto.getPage() - 1, notificationsRequestDto.getSize(), sort);

        Page<Notification> page = notificationRepository.findAll(spec, pageable);

        List<NotificationDto> dtoList = page.getContent().stream()
                .map(NotificationMapper.INSTANCE::toNotificationDto)
                .collect(Collectors.toList());
        Page<NotificationDto> pageDto = new PageImpl<>(dtoList, pageable, page.getTotalElements());

        return pageDto;
    }

    private List<Predicate> buildCriteria(NotificationsRequestDto notificationsRequestDto, Root<Notification> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (notificationsRequestDto.getFilters().getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("filter").get("id"), notificationsRequestDto.getFilters().getId()));
        }

        return predicates;
    }

    public long countUnseenNotifications(Long filterId) {
        return notificationRepository.countByFilterIdAndSeenFalse(filterId);
    }

    public long countByUserIdAndSeenFalse(Long userId, String userEmail) {
        authorize(userId, userEmail);
        return notificationRepository.countUnseenNotificationsByUserId(userId);
    }

    public void markNotificationAsSeen(Long notificationId, String userEmail) {
        authorize(notificationRepository.findById(notificationId).get().getFilter().getUser().getId(), userEmail);
        notificationRepository.markNotificationAsSeen(notificationId, LocalDateTime.now());
    }

    public void markAllNotificationsAsSeenForFilter(Long filterId, String userEmail) {
        authorize(filterRepository.findById(filterId).get().getUser().getId(), userEmail);
        notificationRepository.markAllNotificationsAsSeenForFilter(filterId, LocalDateTime.now());
    }

    @Scheduled(fixedRate = 1000) // Send every 5 seconds
    public void sendNotificationToUser() {
        List<Notification> notifications = notificationRepository.findBySeenFalse();
        notifications.removeAll(sentNotifications);

        if (notifications.size() > 0) {
            for (Notification notification : notifications) {
                String userId = String.valueOf(notification.getRealEstate().getUser().getId());
                String message = "newNotificationReceived";
                messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", message);
                sentNotifications.add(notification);
            }
        }
    }

    private void authorize(Long userId, String userEmail) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent() && !user.get().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized!");
        }
    }
}
