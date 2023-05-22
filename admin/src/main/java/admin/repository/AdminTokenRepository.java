package admin.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminTokenRepository extends CrudRepository<AdminToken, Integer> {
}
