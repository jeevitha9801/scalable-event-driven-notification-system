package com.jeevitha.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationStats {
    private long total;

    private long sent;

    private long failed;

    private long email;
    private long sms;
    private long push;

}
