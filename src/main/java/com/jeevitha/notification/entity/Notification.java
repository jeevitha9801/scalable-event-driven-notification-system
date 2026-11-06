package com.jeevitha.notification.entity;

import com.jeevitha.notification.enums.NotificationChannel;
import com.jeevitha.notification.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    private String recipient;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private Integer retryCount = 0;
}