package cz.cvut.fit.hanespat.data.dto.create;

import lombok.Getter;
import java.util.List;

public class DeliveryAreaCreateDTO {
    @Getter private final String name;
    @Getter private final List<Integer> deliveryBoyIds;

    public DeliveryAreaCreateDTO( String name, List<Integer> deliveryBoyIds ) {
        this.name = name;
        this.deliveryBoyIds = deliveryBoyIds;
    }
}
