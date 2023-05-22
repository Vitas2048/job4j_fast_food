package kitchen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("model")
@EnableJpaRepositories("kitchen.repository")
@SpringBootApplication
public class KitchenApp {

    public static void main(String[] args) {
        SpringApplication.run(KitchenApp.class, args);
    }

}
