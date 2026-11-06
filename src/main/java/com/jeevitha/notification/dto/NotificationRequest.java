package com.jeevitha.notification.dto;

import com.jeevitha.notification.enums.NotificationChannel;
import com.jeevitha.notification.enums.NotificationTemplate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest implements Serializable {

    @NotBlank
    private String userId;

    @NotNull
    private NotificationChannel channel;

    @NotBlank
    private String recipient;

    @NotNull
    private NotificationTemplate template;

}