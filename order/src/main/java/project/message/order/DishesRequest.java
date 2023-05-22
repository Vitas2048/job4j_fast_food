package project.message.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class DishesRequest {

    private List<Integer> dishIds;
}
