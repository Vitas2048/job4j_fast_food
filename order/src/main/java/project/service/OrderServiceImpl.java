package project.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import project.repository.OrderRepository;
import project.repository.StatusRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final Queue<Order> preorders = new LinkedList<>();

    private KafkaTemplate<String, Object> kafkaTemplate;

    private OrderRepository orderRepository;

    private StatusRepository statusRepository;


    @Override
    public Order createOrder(Order order) {
       var save = orderRepository.save(order);
       log.debug("Saved");
       preorders.offer(save);
       log.debug("Queued");
       var transaction = new Transaction();
       var customer = order.getCustomer();
       transaction.setDishes(order.getDishes());
       transaction.setCustomerId(customer.getId());
       transaction.setOrderId(order.getId());
       if (customer.getCard() == null) {
           transaction.setCardDiscount(0);
       } else {
           transaction.setCardDiscount(customer.getCard().getBonus());
       }
       kafkaTemplate.send(Topics.TOPIC_DO_TRANSACTION, transaction);
       return save;
    }

    @Override
    public Optional<Order> findById(int id) {
        return orderRepository.findById(id);
    }

    @Override
    public OrderDTO getOrderDTO(int id) {
        var order = orderRepository.findById(id).get();
        return new OrderDTO(order);
    }
    @KafkaListener(topics = Topics.TOPIC_PAYMENT_RESULT)
    private void getResultFromPayment(PaymentResponse response) {
        var order = orderRepository.findById(response.getOrderId()).get();
        var customer = order.getCustomer().getId();
        var notification = new Notification();

        notification.setCustomerId(customer);
        notification.setCreateTime(LocalDateTime.now());
        if (response.isPaid()) {
            var message = String.format("Order %s WAS SENT TO KITCHEN", order.getId());

            notification.setNoticeText(message);
            order.setCustomer(null);

            kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);
            kafkaTemplate.send(Topics.TOPIC_ORDERS_TO_KITCHEN, order);
            log.debug("sent to kitchen");

        } else {
            var message = String.format("Order %s, Payment denied transaction", order.getId());
            var status = statusRepository.findById(Topics.STATUS_DENIED).get();

            order.setStatus(status);
            notification.setNoticeText(message);
            notification.setCustomerId(customer);

            orderRepository.save(order);
            kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);
            log.debug("Payment denied transaction");
        }
    }

    @KafkaListener(topics = Topics.TOPIC_COMPLETED)
    private void getCompletedFromDelivery(Order order) {
        log.debug("SUCCESSFULLY DELIVERED");
        var message = String.format("Order %s IS SUCCESSFULLY DELIVERED", order.getId());
        var toDto = logAndSave(order, Topics.STATUS_COMPLETED, message);
        kafkaTemplate.send(Topics.TOPIC_GET_INCOME, new OrderDTO(toDto));
    }

    @KafkaListener(topics = Topics.TOPIC_DELIVERING)
    private void getStatusDeliveringFromDelivery(Order order) {
        log.debug("IS DELIVERING");
        var message = String.format("Order %s IS DELIVERING", order.getId());
        logAndSave(order, Topics.STATUS_DELIVERING, message);
    }

    @KafkaListener(topics = Topics.TOPIC_COMPLETED_FROM_KITCHEN)
    private void getCompletedFromKitchen(Order order) {
        log.debug("READY TO PICKUP");
        var message = String.format("Order %s is READY TO PICKUP", order.getId());
        var toDto = logAndSave(order, Topics.STATUS_COMPLETED, message);
        kafkaTemplate.send(Topics.TOPIC_GET_INCOME, new OrderDTO(toDto));
    }

    @KafkaListener(topics = Topics.TOPIC_DENIED)
    private void getDeniedFromKitchen(Order order) {
        log.debug("DENIED FROM KITCHEN");
        var message = String.format("Order %s was DENIED FROM KITCHEN", order.getId());

        var customer = orderRepository.findById(order.getId()).get().getCustomer().getId();
        var money = new MoneyDto();

        money.setCustomerId(customer);
        money.setSum(order.getTotalSum());

        kafkaTemplate.send(Topics.TOPIC_TOP_UP_BALANCE, money);

        logAndSave(order, Topics.STATUS_DENIED, message);
    }

    private Order logAndSave(Order order, int status, String text) {
        var saveStatus = statusRepository.findById(status).get();
        var saveOrder = orderRepository.findById(order.getId()).get();
        saveOrder.setStatus(saveStatus);
        orderRepository.save(saveOrder);
        var notification = new Notification();
        notification.setNoticeText(text);
        notification.setCreateTime(LocalDateTime.now());
        notification.setCustomerId(saveOrder.getCustomer().getId());
        kafkaTemplate.send(Topics.TOPIC_DO_NOTICE, notification);
        log.debug(String.format("Order id = %s, status = %s",
                order.getId(), saveOrder.getStatus().getName()));
        return saveOrder;
    }
}
