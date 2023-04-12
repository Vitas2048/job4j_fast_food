package project.service;

import model.Order;
import model.OrderDTO;
import model.Status;

import java.util.Optional;

public interface OrderService {

    void createOrder(Order order);

    Status checkStatus(int orderId);

    Optional<Order> findById(int id);

    OrderDTO getOrderDTO(int id);

}
