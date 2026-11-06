package com.jeevitha.notification.dto;

import com.jeevitha.notification.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long notificationId;

    private String userId;

    private NotificationChannel channel;

    private String recipient;

    private String message;

}
