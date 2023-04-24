package kitchen.repository;

import model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.LinkedList;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
