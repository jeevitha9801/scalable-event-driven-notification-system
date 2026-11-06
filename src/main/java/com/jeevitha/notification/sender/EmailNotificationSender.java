package com.jeevitha.notification.sender;

import com.jeevitha.notification.dto.NotificationEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender{
    @Override
    public void send(NotificationEvent event) {

        validateServiceAvailability();

        System.out.println(
                "Sending EMAIL to : "
                        + event.getRecipient()
        );

        System.out.println(
                event.getMessage()
        );
    }
}
