package dish.service;

import model.Dish;

import java.util.List;

public interface DishService {

    void addDish(Dish dish);

    void deleteDish(Dish dish);

    void repriceDish(Dish dish, int newPrice);

    void renameDish(Dish dish, String newName);

    Dish findById(int id);

    List<Dish> findAll();

}
