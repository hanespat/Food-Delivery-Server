package cz.cvut.fit.hanespat.data.dto;

import lombok.Getter;
import java.util.List;

/**
 * DTO (Data transfer object) used for data encapsulation when creating object without id.
 */
public class DeliveryBoyCreateDTO {

    @Getter private final String name;
    @Getter private final String surname;
    @Getter private final String phoneNumber;
    @Getter private final List<Integer> deliveryAreaIds;

    public DeliveryBoyCreateDTO(String name, String surname, String phoneNumber, List<Integer> deliveryAreaIds) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.deliveryAreaIds = deliveryAreaIds;
    }
}
