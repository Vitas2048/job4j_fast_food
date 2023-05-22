package payment.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import payment.repository.BalanceRepository;
import payment.repository.DishRepository;
import payment.repository.TransactionRepository;
import model.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository repository;

    private BalanceRepository balanceRepository;

    private DishRepository dishRepository;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = Topics.TOPIC_DO_TRANSACTION)
    private void isAccepted(Transaction transaction) {
        transaction.getDishes().forEach(dishRepository::save);
        var balance = balanceRepository.getByCustomerId(transaction.getCustomerId());
        var total = transaction.getDishes().stream().mapToInt(Dish::getPrice).sum();
        var discount = transaction.getCardDiscount() / 100;
        if (transaction.getCardDiscount() != 0) {
            total = total * discount;
        }
        var res = balance.getSum() - total;
        repository.save(transaction);
        if (res >= 0) {
            kafkaTemplate.send(Topics.TOPIC_PAYMENT_RESULT, new PaymentResponse(transaction.getOrderId(), true));
            balance.setSum(res);
            balanceRepository.save(balance);
            kafkaTemplate.send(Topics.TOPIC_GET_BALANCE_PAYMENT, balance);
        } else {
            kafkaTemplate.send(Topics.TOPIC_PAYMENT_RESULT, new PaymentResponse(transaction.getOrderId(), false));
        }
    }
}
