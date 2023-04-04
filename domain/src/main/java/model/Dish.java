package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Dish {

    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private int price;

    private Kitchen kitchen;
}
