package project.controller;

import lombok.AllArgsConstructor;
import model.Card;
import model.Dish;
import model.Order;
import model.OrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.service.CardServiceImpl;
import project.service.OrderServiceImpl;

import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {

    private final CardServiceImpl cardService;

    private OrderServiceImpl orderService;

    @GetMapping("buy-card")
    public ResponseEntity getAllOrders() {
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
        orderService.createOrder(order);
        var body = new HashMap<>() {{
            put("new order", "created");
        }}.toString();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("{id}")
    public ResponseEntity getOrderDetails(@PathVariable int id) {
        var orderDto = orderService.getOrderDTO(id);
        var body = orderDto.toString();
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @GetMapping("{id}/get-status")
    public ResponseEntity getStatus(@PathVariable int id) {
        var status = orderService.checkStatus(id).getName();
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
