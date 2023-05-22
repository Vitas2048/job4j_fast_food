package dish.service;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import dish.repository.DishRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private DishRepository dishRepository;

    private KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void addDish(Dish dish) {
      dishRepository.save(dish);

      kafkaTemplate.send(Topics.TOPIC_SEND_DISH_ADMIN, dish);
      kafkaTemplate.send(Topics.TOPIC_SEND_DISH_DELIVERY, dish);
      kafkaTemplate.send(Topics.TOPIC_SEND_DISH_KITCHEN, dish);
      kafkaTemplate.send(Topics.TOPIC_SEND_DISH_ORDER, dish);
      kafkaTemplate.send(Topics.TOPIC_SEND_DISH_PAYMENT, dish);
    }

    @Override
    public void deleteDish(Dish dish) {
        dishRepository.delete(dish);
    }

    @Override
    public void repriceDish(Dish dish, int newPrice) {
        dish.setPrice(newPrice);
    }

    @Override
    public void renameDish(Dish dish, String newName) {
        dish.setName(newName);
        dishRepository.save(dish);
    }

    @Override
    public Dish findById(int id) {
        var dish = dishRepository.findById(id);
        return dish.get();
    }

    @Override
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public void sendAllDishes() {
        List<Dish> dishes = findAll();
        String json = new Gson().toJson(dishes);
        kafkaTemplate.send(Topics.TOPIC_ALL_DISHES_ADMIN, json);
        kafkaTemplate.send(Topics.TOPIC_ALL_DISHES_DELIVERY, json);
        kafkaTemplate.send(Topics.TOPIC_ALL_DISHES_KITCHEN, json);
        kafkaTemplate.send(Topics.TOPIC_ALL_DISHES_ORDER, json);
        kafkaTemplate.send(Topics.TOPIC_ALL_DISHES_PAYMENT, json);
    }


    @KafkaListener(topics = Topics.TOPIC_REMOVE_DISH)
    private void removeDish(IdDTO id) {
        dishRepository.deleteById(id.getId());
    }
}
