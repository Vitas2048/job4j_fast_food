package admin.repository;

import model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepository extends CrudRepository<Admin, Integer> {

    Optional<Admin> findByLogin(String login);

    Admin getByLogin(String login);

    @Query("""
           SELECT a FROM Admin AS a
           WHERE a.login = ?1
           AND (a.token.accessToken = ?2 OR a.token.refreshToken = ?2)
           """)
    Optional<Admin> findByLoginAndToken(String login, String token);
}
