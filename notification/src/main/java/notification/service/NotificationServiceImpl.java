package notification.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Notification;
import model.Order;
import notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    @Override
    @KafkaListener(topics = "job4j_orders")
    public void receiveOrderSaveNotice(Order order) {
        var notice = new Notification();
        notice.setNoticeText(order.toString() + " is created");
        notificationRepository.save(notice);
        log.debug(notice.getNoticeText());
    }
}
