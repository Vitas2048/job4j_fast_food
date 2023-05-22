package notification.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl {

    private NotificationRepository notificationRepository;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = Topics.TOPIC_DO_NOTICE)
    public void receiveNotificationSaveAndSend(Notification notification) {
        notificationRepository.save(notification);
        log.debug(notification.getNoticeText());
        var customerId = notification.getCustomerId();
        var notificationDto = new NotificationDTO(customerId, notificationRepository.getByCustomerId(customerId));
        kafkaTemplate.send(Topics.TOPIC_GET_NOTICE, notificationDto);
    }
}
