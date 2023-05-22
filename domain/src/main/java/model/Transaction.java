package model;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food_transaction")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int customerId;

    private int cardDiscount;

    private int orderId;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "trans_dishes",
            joinColumns = { @JoinColumn(name = "trans_id") },
            inverseJoinColumns = { @JoinColumn(name = "dish_id") }
    )
    private List<Dish> dishes = new ArrayList<>();
}
