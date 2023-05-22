package model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "balance")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Balance {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int sum;

    private int customerId;
}
