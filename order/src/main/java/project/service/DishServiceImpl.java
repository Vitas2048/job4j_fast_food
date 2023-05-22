package project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import project.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishServiceImpl implements DishService {

    private DishRepository repository;

    @Override
    public List<Dish> getAllDishes() {
        return repository.findAll();
    }

    @Override
    public Dish findById(int id) {
        return repository.findById(id);
    }

    @Override
    public int countTotal(List<Dish> dishes) {
        return dishes.stream().mapToInt(Dish::getPrice).sum();
    }

    @KafkaListener(topics = Topics.TOPIC_SEND_DISH_ORDER)
    private void saveDish(Dish dish) {
        repository.save(dish);
    }

    @KafkaListener(topics = Topics.TOPIC_REMOVE_DISH_ORDER)
    private void deleteDish(IdDTO id) {
        repository.deleteById(id.getId());
    }

    @KafkaListener(topics = Topics.TOPIC_ALL_DISHES_ORDER)
    private void savAllDishes(String jsonDishes) {
        try {
            List<Dish> dishes = new ObjectMapper().readValue(jsonDishes, new TypeReference<List<Dish>>() { });
            dishes.forEach(p -> repository.save(p));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
