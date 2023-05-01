package dish.service;

import lombok.AllArgsConstructor;
import model.Dish;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import dish.repository.DishRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class DishServiceImpl implements DishService {

    private DishRepository dishRepository;

    @Override
    public void addDish(Dish dish) {
      dishRepository.save(dish);
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
}
