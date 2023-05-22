package project.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.message.registration.RegistrationRequest;
import project.repository.CustomerRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {


    private CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final Map<Integer, NotificationDTO> NOTIFICATION = new HashMap<>();

    public static final Map<Integer, Integer> BALANCE = new HashMap<>();

    public Customer get() {
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void register(RegistrationRequest request) {
        final Customer customer = new Customer();

        customer.setLogin(request.getLogin());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);

        kafkaTemplate.send(Topics.TOPIC_GET_CUSTOMER_ADMIN, new CustomerDTO(customer));
        kafkaTemplate.send(Topics.TOPIC_CREATE_CUSTOMERS_BALANCE, customer.getId());
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Integer getBalance() {
       return BALANCE.get(get().getId());
    }

    public NotificationDTO getNotifications() {
        var notificationDto = NOTIFICATION.get(get().getId());
        return notificationDto;
    }

    public String topUpBalance(int sum) {
        var money = new MoneyDto();
        money.setSum(sum);
        money.setCustomerId(get().getId());
        kafkaTemplate.send(Topics.TOPIC_TOP_UP_BALANCE, money);
        return "Request sent";
    }

    @KafkaListener(topics = Topics.TOPIC_REMOVE_CUSTOMER)
    private void removeCustomer(IdDTO id) {
        customerRepository.deleteById(id.getId());
    }

    @KafkaListener(topics = Topics.TOPIC_GET_BALANCE_PAYMENT)
    private void putBalance(Balance balance) {
        BALANCE.put(balance.getCustomerId(), balance.getSum());
    }

    @KafkaListener(topics = Topics.TOPIC_GET_NOTICE)
    private void getNotice(NotificationDTO notificationDTO) {
        NOTIFICATION.put(notificationDTO.getCustomerId(), notificationDTO);
    }
}
