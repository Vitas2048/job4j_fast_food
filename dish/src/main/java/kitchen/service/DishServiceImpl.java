package kitchen.service;

import lombok.AllArgsConstructor;
import model.Dish;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import repository.DishRepository;

@AllArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private final RestTemplate restTemplate;

    @Value("/dish/")
    private String url;

    private DishRepository dishRepository;

    @Override
    public void addDish(Dish dish) {
      dishRepository.save(dish);
      restTemplate.postForEntity(url, dish, Dish.class);
    }

    @Override
    public void deleteDish(Dish dish) {
        dishRepository.delete(dish);
        restTemplate.exchange(String.format("%s?id=%s", url, dish.getId()),
                HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
    }

    @Override
    public void repriceDish(Dish dish, int newPrice) {
        dish.setPrice(newPrice);
        dishRepository.save(dish);
        restTemplate.exchange(String.format("%s?id=%s", url, dish.getId()),
                HttpMethod.PUT, new HttpEntity<>(dish), Void.class);
    }

    @Override
    public void renameDish(Dish dish, String newName) {
        dish.setName(newName);
        dishRepository.save(dish);
        restTemplate.exchange(String.format("%s?id=%s", url, dish.getId()),
                HttpMethod.PUT, new HttpEntity<>(dish), Void.class);
    }

    @Override
    public Dish findById(int id) {
        var dish = dishRepository.findById(id);
        return restTemplate.postForEntity(String.format("%s/findById?id=%s", url, id), dish, Dish.class).getBody();
    }
}
