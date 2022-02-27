package cz.cvut.fit.hanespat.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import javax.persistence.*;

@Entity
@NoArgsConstructor
public class DeliveryArea {

    @Id
    @GeneratedValue
    @Getter
    private int id;

    @Column(nullable = false)
    @Getter @Setter
    private String name;

    @ManyToMany
    @JoinTable (
            name = "delivery_area_delivery_boy",
            joinColumns = @JoinColumn( name = "delivery_area_id" ),
            inverseJoinColumns = @JoinColumn( name = "delivery_boy_id" )
    )
    @Getter @Setter
    private List<DeliveryBoy> deliveryBoys;

    public DeliveryArea(String name, List<DeliveryBoy> deliveryBoys) {
        this.name = name;
        this.deliveryBoys = deliveryBoys;
    }
}
