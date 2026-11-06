package com.jeevitha.notification.consumer;

import com.jeevitha.notification.config.RabbitMqConfig;
import com.jeevitha.notification.dto.NotificationEvent;
import com.jeevitha.notification.entity.Notification;
import com.jeevitha.notification.enums.NotificationStatus;
import com.jeevitha.notification.repository.NotificationRepository;
import com.jeevitha.notification.sender.EmailNotificationSender;
import com.jeevitha.notification.sender.PushNotificationSender;
import com.jeevitha.notification.sender.SmsNotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository repository;

    private final RabbitTemplate rabbitTemplate;

    private final EmailNotificationSender emailSender;

    private final SmsNotificationSender smsSender;

    private final PushNotificationSender pushSender;


    @RabbitListener(
            queues = RabbitMqConfig.QUEUE_NAME
    )
    public void consume(
            NotificationEvent event) {

        System.out.println(
                "Consumed By -> "
                        + Thread.currentThread().getName()
        );

        Notification notification =
                repository.findById(event.getNotificationId())
                        .orElseThrow();

        notification.setStatus(NotificationStatus.PROCESSING);

        repository.save(notification);

        System.out.println("Processing notification...");

        // simulate sending email/sms
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            try {

                System.out.println("Attempt : " + attempt);

                switch (event.getChannel()) {

                    case EMAIL ->
                            emailSender.send(event);

                    case SMS ->
                            smsSender.send(event);

                    case PUSH ->
                            pushSender.send(event);
                }

                notification.setStatus(NotificationStatus.SENT);
                repository.save(notification);

                System.out.println("Notification sent successfully");

                break; // stop retrying

            } catch (Exception ex) {

                notification.setRetryCount(attempt);

                repository.save(notification);

                System.out.println("Attempt failed : " + attempt);

                if (attempt == maxRetries) {

                    notification.setStatus(NotificationStatus.FAILED);

                    repository.save(notification);

                    rabbitTemplate.convertAndSend(
                            RabbitMqConfig.DLQ_NAME,
                            event
                    );

                    System.out.println("Moved to Dead Letter Queue");
                }
            }
        }
    }
}
