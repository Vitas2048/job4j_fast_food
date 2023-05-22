package kitchen.service;

import kitchen.repository.DishRepository;
import kitchen.repository.OrderRepository;
import lombok.AllArgsConstructor;
import model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@Service
@Slf4j
@AllArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private OrderRepository orderRepository;

    private final Queue<Order> cookedOrder = new LinkedList<>();

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOrderToOrder(Order order, String topic) {
        orderRepository.save(order);
        kafkaTemplate.send(topic, order);
        log.debug(order.toString());
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.findById(id).get();
    }

    @Override
    public Optional<Order> getOrderFromQueue() {
        return Optional.ofNullable(cookedOrder.poll());
    }

    @Override
    public Optional<Order> peekOrderFromQueue() {
        return Optional.ofNullable(cookedOrder.peek());
    }

    @KafkaListener(topics = Topics.TOPIC_ORDERS_TO_KITCHEN)
    private void receiveOrder(Order order) {
        orderRepository.save(order);
        cookedOrder.offer(order);
        log.debug(order.toString());
    }

}
