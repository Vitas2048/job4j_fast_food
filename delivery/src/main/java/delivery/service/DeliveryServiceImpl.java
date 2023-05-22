package delivery.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import delivery.repository.DishRepository;
import delivery.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private OrderRepository orderRepository;

    private DishRepository dishRepository;

    private KafkaTemplate<String, Object> kafkaTemplate;


    private final Queue<Order> deliveryQueue = new LinkedList<>();

    private final Queue<Order> isDeliveringQueue = new LinkedList<>();

    @Override
    public void sendOrderToDelivery(Order order) {
        orderRepository.save(order);
        isDeliveringQueue.add(order);
        kafkaTemplate.send(Topics.TOPIC_DELIVERING, order);
        log.debug("ORDER IS DELIVERING NOW :" + order);
    }

    @Override
    public Order sendDeliveredOrderToOrder() {
        var order = Optional.ofNullable(isDeliveringQueue.poll());
        if (order.isPresent()) {
            kafkaTemplate.send(Topics.TOPIC_COMPLETED, order.get());
            log.debug("SEND TO ORDER SERVICE: " + order);
            return order.get();
        }
        log.debug("DELIVERY QUEUE IS EMPTY");
        return order.get();
    }

    @Override
    public Optional<Order> getFromQueue() {
        return Optional.ofNullable(deliveryQueue.poll());
    }

    @Override
    public Optional<Order> peekFromQueue() {
        return Optional.ofNullable(deliveryQueue.peek());
    }

    @KafkaListener(topics = Topics.TOPIC_TO_DELIVERY_SERVICE)
    private void receiveOrder(Order order) {
        orderRepository.save(order);
        deliveryQueue.add(order);
        log.debug(order.toString());
    }

    @KafkaListener(topics = Topics.TOPIC_ALL_DISHES_DELIVERY)
    private void getDishes(String jsonDishes) {
        try {
            List<Dish> dishes = new ObjectMapper().readValue(jsonDishes, new TypeReference<List<Dish>>() { });
            dishes.forEach(p -> dishRepository.save(p));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = Topics.TOPIC_SEND_DISH_DELIVERY)
    private void saveDish(Dish dish) {
        dishRepository.save(dish);
    }

    @KafkaListener(topics = Topics.TOPIC_REMOVE_DISH_DELIVERY)
    private void deleteDish(IdDTO id) {
        dishRepository.deleteById(id.getId());
    }

}
