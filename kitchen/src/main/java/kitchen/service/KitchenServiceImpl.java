package kitchen.service;

import model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KitchenServiceImpl implements KitchenService {
    @Override
    @KafkaListener(topics = "job4j_orders")
    public void receiveOrder(Order order) {
        log.debug(order.toString());
    }
}
