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

import java.util.HashMap;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final CardServiceImpl cardService;

    private OrderServiceImpl orderService;

    @GetMapping("buy-card")
    public ResponseEntity<String> getAllOrders() {
        cardService.buyCard(new Card());
        var body = new HashMap<>() {{
            put("new discount card", "created");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @PostMapping("/create-order")
    public ResponseEntity<String> createNewOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        var body = new HashMap<>() {{
            put("new order", "created");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("{id}/get-status")
    public ResponseEntity<String> getStatus(@PathVariable int id) {
        var status = orderService.checkStatus(id).getName();
        var body = new HashMap<>() {{
            put("Status", status);
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }
}
