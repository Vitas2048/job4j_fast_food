package admin.service;

import admin.config.app.ApplicationConfig.DefaultAdmin;
import admin.message.registration.RegistrationRequest;
import admin.repository.AdminRepository;
import admin.repository.CustomerDtoRepository;
import admin.repository.DishRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class AdminService {

    private DishRepository dishRepository;

    private CustomerDtoRepository customerDtoRepository;

    private AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    private KafkaTemplate<String, Object> kafkaTemplate;

    private List<OrderDTO> orders;

    public void init(DefaultAdmin defaultAdmin) {
        if (adminRepository.findByLogin(defaultAdmin.getLogin()).isEmpty()) {
            final Admin admin = new Admin();

            admin.setLogin(defaultAdmin.getLogin());
            admin.setPassword(passwordEncoder.encode(defaultAdmin.getPassword()));

            adminRepository.save(admin);

            log.debug("Default admin successfully created");
        } else {
            log.debug("Default admin already existed. Skip creation");
        }
    }

    public Admin get() {
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void register(RegistrationRequest request) {
        final Admin admin = new Admin();

        admin.setLogin(request.getLogin());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        adminRepository.save(admin);
    }

    public void removeDish(IdDTO id) {
        dishRepository.deleteById(id.getId());
        kafkaTemplate.send(Topics.TOPIC_REMOVE_DISH, id);
        kafkaTemplate.send(Topics.TOPIC_REMOVE_DISH_DELIVERY, id);
        kafkaTemplate.send(Topics.TOPIC_REMOVE_DISH_KITCHEN, id);
        kafkaTemplate.send(Topics.TOPIC_REMOVE_DISH_ORDER, id);
        kafkaTemplate.send(Topics.TOPIC_REMOVE_DISH_PAYMENT, id);
    }

    public void removeCustomer(IdDTO id) {
        customerDtoRepository.deleteById(id.getId());
        kafkaTemplate.send(Topics.TOPIC_REMOVE_CUSTOMER, id);
    }

    public IncomeDto getIncomeDto() {
        var income = new IncomeDto();
        income.setTotal(orders.stream().mapToInt(OrderDTO::getTotal).sum());
        income.setOrders(orders);
        return income;
    }

    public List<Dish> getDishes() {
        return dishRepository.findAll();
    }

    public List<CustomerDTO> getCustomers() {
        return customerDtoRepository.findAll();
    }

    @KafkaListener(topics = Topics.TOPIC_GET_CUSTOMER_ADMIN)
    private void getIncome(CustomerDTO customerDTO) {
        customerDtoRepository.save(customerDTO);
    }

    @KafkaListener(topics = Topics.TOPIC_GET_INCOME)
    private void getIncome(OrderDTO orderDTO) {
        this.orders.add(orderDTO);
    }

    @KafkaListener(topics = Topics.TOPIC_ALL_DISHES_ADMIN)
    private void savAllDishes(String jsonDishes) {
        try {
            List<Dish> dishes = new ObjectMapper().readValue(jsonDishes, new TypeReference<List<Dish>>() { });
            dishes.forEach(p -> dishRepository.save(p));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = Topics.TOPIC_SEND_DISH_ADMIN)
    private void saveDish(Dish dish) {
        dishRepository.save(dish);
    }
}
