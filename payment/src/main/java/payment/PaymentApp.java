package payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import payment.controler.TransactionController;

@ComponentScan("model")
@EntityScan("model")
@EnableJpaRepositories("payment.repository")
@SpringBootApplication
@ComponentScan(basePackageClasses = TransactionController.class)
public class PaymentApp {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }
}
