package project.service;

import model.*;

import java.util.Optional;

public interface OrderService {

    void createOrder(Order order);

    Optional<Order> findById(int id);

    OrderDTO getOrderDTO(int id);

}
