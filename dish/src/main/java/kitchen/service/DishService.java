package kitchen.service;

import model.Dish;

public interface DishService {

    void addDish(Dish dish);

    void deleteDish(Dish dish);

    void repriceDish(Dish dish, int newPrice);

    void renameDish(Dish dish, String newName);

    Dish findById(int id);

}
