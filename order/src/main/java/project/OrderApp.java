package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.controller.AuthController;
import project.controller.CustomerController;
import project.controller.OrderController;
import project.controller.RegistrationController;


@ComponentScan("model")
@EntityScan("model")
@EnableJpaRepositories("project.repository")
@SpringBootApplication
@ComponentScan(basePackageClasses = OrderController.class)
@ComponentScan(basePackageClasses = RegistrationController.class)
@ComponentScan(basePackageClasses = AuthController.class)
@ComponentScan(basePackageClasses = CustomerController.class)
public class OrderApp {

    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
