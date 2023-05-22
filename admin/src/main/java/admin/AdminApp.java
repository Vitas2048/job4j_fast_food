package admin;

import admin.controller.AccountController;
import admin.controller.AuthController;
import admin.controller.RegistrationController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ComponentScan("model")
@EntityScan("model")
@EnableJpaRepositories("admin.repository")
@ComponentScan(basePackageClasses = AccountController.class)
@ComponentScan(basePackageClasses = AuthController.class)
@ComponentScan(basePackageClasses = RegistrationController.class)
@SpringBootApplication
public class AdminApp {

    public static void main(String[] args) {
        SpringApplication.run(AdminApp.class, args);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
