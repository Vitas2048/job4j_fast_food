package project.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.message.account.CustomerDTO;
import project.message.order.DishesRequest;
import project.service.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@Slf4j
public class OrderController {

    private CardServiceImpl cardService;

    private OrderServiceImpl orderService;

    private StatusServiceImpl statusService;

    private DishService dishService;

    private CustomerService customerService;

    @GetMapping("checkout")
    public ResponseEntity<CustomerDTO> check() {
        var body = customerService.get();
        return ResponseEntity.ok(new CustomerDTO(body));
    }

    @PostMapping("/buy-card")
    public ResponseEntity buyCard() {
        var card = new Card();
        card.setBonus(10);
        cardService.buyCard(card, customerService.get());
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
    public ResponseEntity<OrderDTO> createNewOrder(@RequestBody DishesRequest dishesRequest) {

        var dishes = dishesRequest.getDishIds().stream().map(p -> dishService.findById(p)).toList();
        var sum = dishService.countTotal(dishes);
        var customer = customerService.get();

        var order = new Order();
        order.setDishes(dishes);
        order.setTotalSum(sum);
        order.setCustomer(customer);
        order.setStatus(statusService.getStatusById(Topics.STATUS_CREATED));

        customer.getOrders().add(order);
        orderService.createOrder(order);

        return ResponseEntity.ok(new OrderDTO(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable int id) {
        var body = orderService.getOrderDTO(id);
        if (body.getCustomerId() == customerService.get().getId()) {
            return ResponseEntity.ok(body);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("get-dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        var dishes = dishService.getAllDishes();
        return ResponseEntity.ok(dishes);
    }
}
