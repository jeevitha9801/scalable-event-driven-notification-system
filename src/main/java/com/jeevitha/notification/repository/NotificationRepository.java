package com.jeevitha.notification.repository;

import com.jeevitha.notification.entity.Notification;
import com.jeevitha.notification.enums.NotificationChannel;
import com.jeevitha.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByStatus(
            NotificationStatus status
    );

    long countByStatus(NotificationStatus status);

    long countByChannel(NotificationChannel channel);


}
