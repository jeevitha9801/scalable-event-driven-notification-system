package com.jeevitha.notification.sender;

import com.jeevitha.notification.dto.NotificationEvent;

public interface NotificationSender {
    void send(NotificationEvent event);
    default void validateServiceAvailability() {

        if (Math.random() > 0.95) {
            throw new RuntimeException("Service down");
        }
    }
}
