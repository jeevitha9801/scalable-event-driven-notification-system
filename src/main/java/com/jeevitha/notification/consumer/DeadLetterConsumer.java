package com.jeevitha.notification.consumer;

import com.jeevitha.notification.config.RabbitMqConfig;
import com.jeevitha.notification.dto.NotificationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterConsumer {
    @RabbitListener(
            queues = RabbitMqConfig.DLQ_NAME
    )
    public void consume(NotificationEvent event) {

        System.out.println(
                "DLQ Message Received -> " + event
        );
    }
}
