package project.message.account;

import lombok.Getter;
import lombok.Setter;
import model.*;

import java.util.List;

@Getter@Setter
public class CustomerDTO {

    private int id;

    private String login;

    private List<OrderDTO> orders;


    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.login = customer.getLogin();
        this.orders = customer.getOrders().stream().map(OrderDTO::new).toList();
    }

}
