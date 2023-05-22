package kitchen.controller;

import kitchen.service.KitchenService;
import kitchen.service.StatusService;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/kitchen")
public class KitchenController {

    private KitchenService kitchenService;

    private StatusService statusService;

    @GetMapping("peek-order")
    public ResponseEntity<Order> peekOrder() {
        var order = kitchenService.peekOrderFromQueue();
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("get-order/deny")
    public ResponseEntity<Order> denyOrder() {
        var orderOpt = kitchenService.getOrderFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_DENIED));
        kitchenService.sendOrderToOrder(order, Topics.TOPIC_DENIED);
        return ResponseEntity.ok(order);
    }

    @PostMapping("get-order/to-delivery")
    public ResponseEntity<Order> completeOrderToDelivery() {
        var orderOpt = kitchenService.getOrderFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_PROCESSING));
        kitchenService.sendOrderToOrder(order, Topics.TOPIC_TO_DELIVERY_SERVICE);
        return ResponseEntity.ok(order);
    }

    @PostMapping("get-order/complete-pickup")
    public ResponseEntity<Order> completeOrder() {
        var orderOpt = kitchenService.getOrderFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_COMPLETED));
        kitchenService.sendOrderToOrder(order, Topics.TOPIC_COMPLETED_FROM_KITCHEN);
        return ResponseEntity.ok(order);
    }

}
