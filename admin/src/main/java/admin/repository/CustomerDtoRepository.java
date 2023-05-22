package admin.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerDtoRepository extends CrudRepository<CustomerDTO, Integer> {

    List<CustomerDTO> findAll();

}
