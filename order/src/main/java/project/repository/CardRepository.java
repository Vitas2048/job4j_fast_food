package project.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;


public interface CardRepository extends CrudRepository<Card, Integer> {
}
