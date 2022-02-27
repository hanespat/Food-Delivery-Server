package cz.cvut.fit.hanespat.data.dto;

import lombok.Getter;

public class AddressDTO {
    @Getter private final int id;
    @Getter private final int houseNumber;
    @Getter private final String street;
    @Getter private final String zipCode;
    @Getter private final int deliveryAreaId;

    public AddressDTO(int id, int houseNumber, String street, String zipCode, int deliveryAreaId) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.deliveryAreaId = deliveryAreaId;
    }
}

