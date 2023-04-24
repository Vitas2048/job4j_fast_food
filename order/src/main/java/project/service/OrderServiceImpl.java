package project.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.Dish;
import model.Order;
import model.OrderDTO;
import model.Topics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.repository.OrderRepository;
import project.repository.StatusRepository;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final Queue<Order> preorders = new LinkedList<>();

    private KafkaTemplate<String, Object> kafkaTemplate;

    private OrderRepository orderRepository;


    @Override
    public Order createOrder(Order order) {
       var save = orderRepository.save(order);
       preorders.offer(save);
       kafkaTemplate.send("job4j_orders", order);
       return save;
    }



    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public OrderDTO getOrderDTO(int id) {
        var order = orderRepository.findById(id).get();
        return new OrderDTO(order.getId(), order.getTotalSum(), order.getStatus().getName());
    }

    @KafkaListener(topics = Topics.TOPIC_COMPLETED)
    @Override
    public void getCompletedFromKitchen(Order order) {
        logAndSave(order, 3);
    }

    @KafkaListener(topics = Topics.TOPIC_DENIED)
    @Override
    public void getDeniedFromKitchen(Order order) {
        logAndSave(order, 4);
    }

    private void logAndSave(Order order, int status) {
        log.debug(String.format("Oder id = %s, status = %s",
                order.getId(), order.getStatus().getName()));
        orderRepository.save(order);
    }
}
