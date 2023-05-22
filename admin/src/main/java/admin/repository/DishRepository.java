package admin.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Integer> {

    List<Dish> findAll();
}
