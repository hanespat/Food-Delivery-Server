package cz.cvut.fit.hanespat.data.dto;

import lombok.Getter;
import java.util.List;

public class DeliveryAreaDTO {
    @Getter private final int id;
    @Getter private final String name;
    @Getter private final List<Integer> deliveryBoyIds;

    public DeliveryAreaDTO(int id, String name, List<Integer> deliveryBoyIds) {
        this.id = id;
        this.name = name;
        this.deliveryBoyIds = deliveryBoyIds;
    }
}
