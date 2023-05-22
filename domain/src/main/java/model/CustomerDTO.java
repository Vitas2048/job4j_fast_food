package model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
@Table(name = "customer_dto")
public class CustomerDTO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private int id;

    private String login;

    public CustomerDTO(Customer customer) {
        id = customer.getId();
        login = customer.getLogin();
    }
}
