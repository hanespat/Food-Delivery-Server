package cz.cvut.fit.hanespat.data.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class DeliveryAreaCreateDTO {
    @Getter private String name;
    @Getter private List<Integer> deliveryBoyIds;

    public DeliveryAreaCreateDTO( String name, List<Integer> deliveryBoyIds ) {
        this.name = name;
        this.deliveryBoyIds = deliveryBoyIds;
    }
}
