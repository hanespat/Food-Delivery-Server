package cz.cvut.fit.hanespat.data.dto.create;

import java.util.List;

public record DeliveryAreaCreateDTO ( String name, List<Integer> deliveryBoyIds ) {}
