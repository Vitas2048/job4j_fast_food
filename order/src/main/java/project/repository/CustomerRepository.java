package project.repository;

import model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Customer getById(int id);

    Optional<Customer> findByLogin(String login);

    Customer getByLogin(String login);

    @Query("""
            SELECT c FROM Customer AS c
            WHERE c.login = ?1
            AND (c.token.accessToken = ?2 OR c.token.refreshToken = ?2)
            """)
    Optional<Customer> findByLoginAndToken(String login, String token);

}
