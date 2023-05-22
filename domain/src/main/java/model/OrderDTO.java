package model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class OrderDTO {

    private int id;

    private int total;

    private String status;

    private List<Dish> dishes;

    private int customerId;

    public OrderDTO(Order order) {
        this.customerId = order.getCustomer().getId();
        this.id = order.getId();
        this.total = order.getTotalSum();
        this.status = order.getStatus().getName();
        this.dishes = order.getDishes();
    }
}
