package cz.cvut.fit.hanespat.data.dto;

import lombok.Getter;

import java.util.List;

public class DeliveryBoyDTO {

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final String surname;
    @Getter private final String phoneNumber;
    @Getter private final List<Integer> deliveryAreaIds;

    public DeliveryBoyDTO(int id, String name, String surname, String phoneNumber, List<Integer> deliveryAreaIds) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.deliveryAreaIds = deliveryAreaIds;
    }
}

