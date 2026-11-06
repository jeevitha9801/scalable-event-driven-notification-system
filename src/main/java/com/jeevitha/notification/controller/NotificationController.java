package com.jeevitha.notification.controller;

import com.jeevitha.notification.dto.NotificationRequest;
import com.jeevitha.notification.dto.NotificationResponse;
import com.jeevitha.notification.dto.NotificationStats;
import com.jeevitha.notification.entity.Notification;
import com.jeevitha.notification.enums.NotificationStatus;
import com.jeevitha.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
    @Valid @RequestBody NotificationRequest request) {

        NotificationResponse response =
                notificationService.sendNotification(request);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }

    @GetMapping("/{id}")
    public Notification getById(
            @PathVariable Long id) {

        return notificationService.getNotification(id);
    }
    @GetMapping
    public Page<Notification> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        return notificationService.getAllNotifications(page, size);
    }

    @GetMapping("/status/{status}")
    public List<Notification> getByStatus(
            @PathVariable NotificationStatus status) {
        return notificationService.getByStatus(status);
    }

    @GetMapping("/stats")
    public NotificationStats getStats() {

        return notificationService.getStats();
    }


}