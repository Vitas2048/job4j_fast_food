package project.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;

public interface CustomerTokenRepository extends CrudRepository<CustomerToken, Integer> {
}
