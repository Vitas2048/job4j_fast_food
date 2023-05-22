package model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "customer")
@NoArgsConstructor
@Entity
public class Customer {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;

    @ManyToOne
    private Card card;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Order> orders;


    @OneToOne (mappedBy = "customer")
    private CustomerToken token;
}
