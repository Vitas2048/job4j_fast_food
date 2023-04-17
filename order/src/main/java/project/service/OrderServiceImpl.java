package project.service;


import lombok.AllArgsConstructor;
import model.Dish;
import model.Order;
import model.OrderDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.repository.OrderRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private RestTemplate restTemplate;

    private KafkaTemplate<String, Object> kafkaTemplate;

    private OrderRepository orderRepository;

    @Override
    public void createOrder(Order order) {
       var save = orderRepository.save(order);
       kafkaTemplate.send("job4j_orders", order);
    }

    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public OrderDTO getOrderDTO(int id) {
        var order = orderRepository.findById(id).get();
        var dishes = order.getDishes().stream().map(
                p -> Objects.requireNonNull(restTemplate.getForEntity(
                        "/dish/findById?id={id}", Dish.class, p.getId()
                ).getBody()).getName()).toList();
        return new OrderDTO(order.getId(), dishes, order.getTotalSum(), order.getStatus().getName());
    }
}
