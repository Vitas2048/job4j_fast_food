package payment.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance, Integer> {

    Balance getByCustomerId(int customerId);
}
