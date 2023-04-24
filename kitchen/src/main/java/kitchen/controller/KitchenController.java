package kitchen.controller;

import kitchen.service.KitchenService;
import kitchen.service.StatusService;
import lombok.AllArgsConstructor;
import model.Topics;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/kitchen")
public class KitchenController {

    private KitchenService kitchenService;

    private StatusService statusService;

    @GetMapping("peek-order")
    public ResponseEntity peekOrder() {
        var order = kitchenService.peekOrderFromQueue();
        if (order.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(Map.of("Queue", "empty"));
        }
        var body = Map.of("id: ", order.get().getId(), "totalSum:", order.get().getTotalSum()).toString();
        return ResponseEntity.status(HttpStatus.FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @PostMapping("get-order/deny")
    public ResponseEntity denyOrder() {
        var orderOpt = kitchenService.getOrderFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(Map.of("Queue", "empty"));
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_DENIED));
        kitchenService.sendOrderToOrder(order, Topics.TOPIC_DENIED);
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }

    @PostMapping("get-order/complete")
    public ResponseEntity completeOrder() {
        var orderOpt = kitchenService.getOrderFromQueue();
        if (orderOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(Map.of("Queue", "empty"));
        }
        var order = orderOpt.get();
        order.setStatus(statusService.findById(Topics.STATUS_COMPLETED));
        kitchenService.sendOrderToOrder(order, Topics.TOPIC_COMPLETED);
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrder(@PathVariable int id) {
        var order = kitchenService.getOrderById(id);
        var body = Map.of("total :", order.getTotalSum()).toString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

}
