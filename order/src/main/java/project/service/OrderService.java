package project.service;

import model.Order;
import model.Status;

public interface OrderService {

    void createOrder(Order order);

    Status checkStatus(int orderId);

}
