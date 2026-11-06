package com.jeevitha.notification.service;

import com.jeevitha.notification.dto.NotificationRequest;
import com.jeevitha.notification.dto.NotificationResponse;
import com.jeevitha.notification.dto.NotificationStats;
import com.jeevitha.notification.entity.Notification;
import com.jeevitha.notification.enums.NotificationStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {

    NotificationResponse sendNotification(
            NotificationRequest request);

    Notification getNotification(Long id);

    Page<Notification> getAllNotifications(int page,int size);

    List<Notification> getByStatus(
            NotificationStatus status
    );
    NotificationStats getStats();
}