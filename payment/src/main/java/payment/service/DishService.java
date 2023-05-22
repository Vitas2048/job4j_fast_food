package payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import payment.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {

    private DishRepository repository;

    @KafkaListener(topics = Topics.TOPIC_ALL_DISHES_PAYMENT)
    private void getAll(String jsonDishes) {
        try {
            List<Dish> dishes = new ObjectMapper().readValue(jsonDishes, new TypeReference<List<Dish>>() { });
            dishes.forEach(p -> repository.save(p));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = Topics.TOPIC_SEND_DISH_PAYMENT)
    private void saveDish(Dish dish) {
        repository.save(dish);
    }

    @KafkaListener(topics = Topics.TOPIC_REMOVE_DISH_PAYMENT)
    private void deleteDish(IdDTO id) {
        repository.deleteById(id.getId());
    }
}
