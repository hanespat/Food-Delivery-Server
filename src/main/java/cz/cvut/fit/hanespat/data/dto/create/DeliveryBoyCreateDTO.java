package cz.cvut.fit.hanespat.data.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class DeliveryBoyCreateDTO {

    @Getter private String name;
    @Getter private String surname;
    @Getter private String phoneNumber;
    @Getter private List<Integer> deliveryAreaIds;

    public DeliveryBoyCreateDTO(String name, String surname, String phoneNumber, List<Integer> deliveryAreaIds) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.deliveryAreaIds = deliveryAreaIds;
    }
}
