package com.jeevitha.notification.service.template;

import com.jeevitha.notification.enums.NotificationTemplate;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
    public String buildMessage(
            NotificationTemplate template,
            String userId) {

        return switch (template) {

            case WELCOME ->
                    "Welcome " + userId +
                            " to our platform.";

            case PASSWORD_RESET ->
                    "Your password reset request "
                            + "has been received.";

            case ORDER_CONFIRMATION ->
                    "Your order has been confirmed.";
        };
    }
}
