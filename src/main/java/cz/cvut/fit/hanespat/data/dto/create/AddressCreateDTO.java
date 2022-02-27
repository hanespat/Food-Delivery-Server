package cz.cvut.fit.hanespat.data.dto.create;

import lombok.Getter;

public class AddressCreateDTO {
    @Getter private final int houseNumber;
    @Getter private final String street;
    @Getter private final String zipCode;
    @Getter private final int deliveryAreaId;

    public AddressCreateDTO(int houseNumber, String street, String zipCode, int deliveryAreaId ) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.deliveryAreaId = deliveryAreaId;
    }
}

