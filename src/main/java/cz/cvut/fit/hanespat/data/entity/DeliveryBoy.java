package cz.cvut.fit.hanespat.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class DeliveryBoy {

    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @Column(nullable = false)
    @Getter @Setter
    private String surname;

    @Column(nullable = false)
    @Getter @Setter
    private String phoneNumber;

    @ManyToMany
    @JoinTable (
            name = "delivery_area_delivery_boy",
            joinColumns = @JoinColumn( name = "delivery_boy_id" ),
            inverseJoinColumns = @JoinColumn( name = "delivery_area_id" )
    )
    @Getter @Setter
    private List<DeliveryArea> deliveryAreas;

    public DeliveryBoy(String name, String surname, String phoneNumber, List<DeliveryArea> deliveryAreas ) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.deliveryAreas = deliveryAreas;
    }
}
