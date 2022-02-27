package cz.cvut.fit.hanespat.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Column(nullable = false)
    @Getter @Setter
    private int houseNumber;

    @Column(nullable = false)
    @Getter @Setter
    private String street;

    @Column(nullable = false)
    @Getter @Setter
    private String zipcode;

    @ManyToOne
    @JoinColumn( name = "delivery_area_id" )
    @Getter @Setter
    private DeliveryArea deliveryArea;

    public Address(int houseNumber, String street, String zipcode, DeliveryArea deliveryArea) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipcode = zipcode;
        this.deliveryArea = deliveryArea;
    }
}

