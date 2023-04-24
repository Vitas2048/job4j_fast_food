package kitchen.service;

import model.Order;

import java.util.Optional;

public interface KitchenService {

    void receiveOrder(Order order);

    void sendOrderToOrder(Order order, String topic);

    Order getOrderById(int id);

    Optional<Order> getOrderFromQueue();

    Optional<Order> peekOrderFromQueue();
}
