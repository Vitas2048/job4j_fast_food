package delivery.controller;

import delivery.service.DeliveryService;
import delivery.service.StatusService;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
@AllArgsConstructor
public class DeliveryController {

    private DeliveryService deliveryService;

    private StatusService statusService;

    @GetMapping("peek-order")
    public ResponseEntity<Order> peekOrder() {
        var order = deliveryService.peekFromQueue();
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("get-order/start-delivery")
    public ResponseEntity<Order> startDelivery() {
        var orderOpt = deliveryService.getFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_DELIVERING));
        deliveryService.sendOrderToDelivery(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("get-order/complete")
    public ResponseEntity<Order> completeDelivery() {
        var order = deliveryService.sendDeliveredOrderToOrder();
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
