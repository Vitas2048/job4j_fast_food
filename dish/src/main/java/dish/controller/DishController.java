package dish.controller;

import dish.service.DishService;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/dish")
@AllArgsConstructor
public class DishController {

    private DishService dishService;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        var dishes = new HashMap<String, Integer>();
        dishService.findAll().forEach(p -> {
            var name = p.getName();
            var price = p.getPrice();
            dishes.put(name, price);
        });
        var body = dishes.toString();
        dishService.sendAllDishes();
        return ResponseEntity.status(HttpStatus.FOUND)
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(body.length())
                .body(body);
    }

    @PostMapping("/add-dish")
    public ResponseEntity addDish(@RequestBody Dish dish) {
        dishService.addDish(dish);
        return new ResponseEntity(HttpStatus.FOUND);
    }
}
