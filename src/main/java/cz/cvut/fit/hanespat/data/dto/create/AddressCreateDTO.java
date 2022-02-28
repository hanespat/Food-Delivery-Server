package cz.cvut.fit.hanespat.data.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AddressCreateDTO {
    @Getter private int houseNumber;
    @Getter private String street;
    @Getter private String zipCode;
    @Getter private int deliveryAreaId;


    public AddressCreateDTO(int houseNumber, String street, String zipCode, int deliveryAreaId ) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.deliveryAreaId = deliveryAreaId;
    }
}

