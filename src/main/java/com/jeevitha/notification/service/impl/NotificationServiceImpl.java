package com.jeevitha.notification.service.impl;

import com.jeevitha.notification.dto.NotificationEvent;
import com.jeevitha.notification.dto.NotificationRequest;
import com.jeevitha.notification.dto.NotificationResponse;
import com.jeevitha.notification.dto.NotificationStats;
import com.jeevitha.notification.entity.Notification;
import com.jeevitha.notification.enums.NotificationChannel;
import com.jeevitha.notification.enums.NotificationStatus;
import com.jeevitha.notification.producer.NotificationProducer;
import com.jeevitha.notification.repository.NotificationRepository;
import com.jeevitha.notification.service.NotificationService;
import com.jeevitha.notification.service.template.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationProducer producer;

    private final NotificationRepository repository;

    private final TemplateService templateService;

    @Override
    public NotificationResponse sendNotification(
            NotificationRequest request) {
        String message =
                templateService.buildMessage(
                        request.getTemplate(),
                        request.getUserId()
                );

        Notification notification =
                Notification.builder()
                        .userId(request.getUserId())
                        .channel(request.getChannel())
                        .recipient(request.getRecipient())
                        .message(message)
                        .status(NotificationStatus.PENDING)
                        .build();

        Notification savedNotification = repository.save(notification);

        NotificationEvent event =
                NotificationEvent.builder()
                        .notificationId(savedNotification.getId())
                        .userId(request.getUserId())
                        .channel(request.getChannel())
                        .recipient(request.getRecipient())
                        .message(message)
                        .build();

        producer.send(event);

        return new NotificationResponse(
                savedNotification.getId().toString(),
                NotificationStatus.PENDING.name()
        );
    }

    @Override
    public Notification getNotification(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    @Override
    public Page<Notification> getAllNotifications(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("id").descending()
                );

        return repository.findAll(pageable);
    }

    @Override
    public List<Notification> getByStatus(NotificationStatus status) {
        return repository.findByStatus(status);

    }

    @Override
    public NotificationStats getStats() {

        long total = repository.count();

        long sent =
                repository.countByStatus(
                        NotificationStatus.SENT);

        long failed =
                repository.countByStatus(
                        NotificationStatus.FAILED);
        long email =
                repository.countByChannel(
                        NotificationChannel.EMAIL);

        long sms =
                repository.countByChannel(
                        NotificationChannel.SMS);

        long push =
                repository.countByChannel(
                        NotificationChannel.PUSH);

        return new NotificationStats(
                total,
                sent,
                failed,
                email,
                sms,
                push
        );
    }
}