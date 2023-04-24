package project.controller;

import lombok.AllArgsConstructor;
import model.Card;
import model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.service.CardServiceImpl;
import project.service.OrderServiceImpl;
import project.service.StatusServiceImpl;

import java.util.HashMap;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private CardServiceImpl cardService;

    private OrderServiceImpl orderService;

    private StatusServiceImpl statusService;

    @GetMapping("/get")
    public ResponseEntity hello() {
        var body = new HashMap<>() {{
            put("Hello", "World");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @PostMapping("/buy-card")
    public ResponseEntity buyCard() {
        cardService.buyCard(new Card());
        var body = new HashMap<>() {{
            put("new discount card", "created");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @PostMapping("/create-order")
    public ResponseEntity createNewOrder(@RequestBody Order order) {
        order.setStatus(statusService.getStatusById(1));
        var id = orderService.createOrder(order).getId();
        var body = new HashMap<>() {{
            put("new order id", id);
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderDetails(@PathVariable int id) {
        var orderDto = orderService.getOrderDTO(id);
        var body = orderDto.toString();
        return new ResponseEntity<>(orderService.findById(id).get(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/get-status")
    public ResponseEntity getStatus(@PathVariable int id) {
        var status = statusService.getStatusById(id).getName();
        var body = new HashMap<>() {{
            put("Status", status);
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }
}
