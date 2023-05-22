package kitchen.service;

import model.*;

import java.util.Optional;

public interface KitchenService {

    void sendOrderToOrder(Order order, String topic);

    Order getOrderById(int id);

    Optional<Order> getOrderFromQueue();

    Optional<Order> peekOrderFromQueue();
}
