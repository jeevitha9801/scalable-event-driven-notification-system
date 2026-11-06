package com.jeevitha.notification.producer;

import com.jeevitha.notification.config.RabbitMqConfig;
import com.jeevitha.notification.dto.NotificationEvent;
import com.jeevitha.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(NotificationEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.QUEUE_NAME,
                event
        );

        System.out.println(
                "Message published: " + event
        );
    }
}
