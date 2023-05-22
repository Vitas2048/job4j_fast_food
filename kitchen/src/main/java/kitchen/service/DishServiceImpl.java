package kitchen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchen.repository.DishRepository;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DishServiceImpl {

    private DishRepository dishRepository;

    @KafkaListener(topics = Topics.TOPIC_ALL_DISHES_KITCHEN)
    private void saveDishes(String json) {
        try {
            List<Dish> dishes = new ObjectMapper().readValue(json, new TypeReference<List<Dish>>() { });
            dishes.forEach(p -> dishRepository.save(p));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = Topics.TOPIC_SEND_DISH_KITCHEN)
    private void saveDish(Dish dish) {
        dishRepository.save(dish);
    }

    @KafkaListener(topics = Topics.TOPIC_REMOVE_DISH_KITCHEN)
    private void deleteDish(IdDTO id) {
        dishRepository.deleteById(id.getId());
    }
}
