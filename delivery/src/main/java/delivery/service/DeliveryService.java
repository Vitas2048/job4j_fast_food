package delivery.service;

import model.*;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    void sendOrderToDelivery(Order order);

    Order sendDeliveredOrderToOrder();

    Optional<Order> getFromQueue();

    Optional<Order> peekFromQueue();


}
