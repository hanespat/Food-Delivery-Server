package cz.cvut.fit.hanespat.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressDTO {
    @Getter private int id;
    @Getter private int houseNumber;
    @Getter private String street;
    @Getter private String zipCode;
    @Getter private int deliveryAreaId;

    public AddressDTO(int id, int houseNumber, String street, String zipCode, int deliveryAreaId) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.deliveryAreaId = deliveryAreaId;
    }
}

