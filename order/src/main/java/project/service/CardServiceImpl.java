package project.service;

import lombok.AllArgsConstructor;
import model.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import project.repository.CardRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private KafkaTemplate<String, Object> kafkaTemplate;

    private CardRepository cardRepository;

    @Override
    public Card buyCard(Card card, Customer customer) {
        cardRepository.save(card);
        var message = String.format("New Card created for user %s", customer.getLogin());
        var notification = new Notification();
        notification.setCreateTime(LocalDateTime.now());
        notification.setCustomerId(customer.getId());
        notification.setNoticeText(message);
        kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);
        return card;
    }
}
