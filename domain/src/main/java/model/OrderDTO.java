package model;

import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class OrderDTO {

    private int id;

    private List<String> dishes;

    private int total;

    private String status;

}
