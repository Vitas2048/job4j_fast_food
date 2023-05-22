package project.service;

import model.*;

import java.util.List;

public interface DishService {

    List<Dish> getAllDishes();

    Dish findById(int id);

    int countTotal(List<Dish> dishes);

}
