package payment.service;

import lombok.AllArgsConstructor;

import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.repository.BalanceRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class BalanceService {

    private BalanceRepository repository;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = Topics.TOPIC_CREATE_CUSTOMERS_BALANCE)
    private void createBalance(int customerId) {
        var balance = new Balance();
        var notification = new Notification();

        balance.setSum(5000);
        balance.setCustomerId(customerId);
        repository.save(balance);
        kafkaTemplate.send(Topics.TOPIC_GET_BALANCE_PAYMENT, balance);

        var text = String.format("Balance created, Balance = %s", 5000);

        notification.setNoticeText(text);
        notification.setCreateTime(LocalDateTime.now());
        notification.setCustomerId(customerId);
        kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);

    }

    @KafkaListener(topics = Topics.TOPIC_TOP_UP_BALANCE)
    private void topUp(MoneyDto money) {
        var notification = new Notification();
        var customerId = money.getCustomerId();
        var balance = repository.getByCustomerId(customerId);
        balance.setSum(balance.getSum() + money.getSum());
        repository.save(balance);
        notification.setCustomerId(customerId);
        notification.setNoticeText(String.format("balance is topped up by %s", money.getSum()));
        kafkaTemplate.send(Topics.TOPIC_GET_BALANCE_PAYMENT, balance);
        kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);
    }

}
