package kitchen.domain;

import lombok.*;
import model.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@Table(name = "ff_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @ManyToMany
    private List<Dish> order;

    private int totalSum;

    @ManyToOne
    private Status status;

    @ManyToOne
    private Customer customer;

}
