package notification.repository;

import model.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> getByCustomerId(int id);
}
