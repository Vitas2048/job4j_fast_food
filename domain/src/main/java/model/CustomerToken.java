package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter@Setter
@Entity
@Table(name = "customer_token")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    private String accessToken;
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
