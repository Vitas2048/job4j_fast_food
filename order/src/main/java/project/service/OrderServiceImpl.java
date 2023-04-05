package project.service;


import lombok.AllArgsConstructor;
import model.Order;
import model.Status;
import org.springframework.stereotype.Service;
import project.repository.OrderRepository;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Override
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public Status checkStatus(int orderId) {
        return orderRepository.findById(orderId).get().getStatus();
    }
}
