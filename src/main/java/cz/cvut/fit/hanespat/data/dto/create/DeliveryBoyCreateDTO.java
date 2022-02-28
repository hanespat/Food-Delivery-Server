package cz.cvut.fit.hanespat.data.dto.create;

import java.util.List;

public record DeliveryBoyCreateDTO ( String name, String surname, String phoneNumber, List<Integer> deliveryAreaIds ) {}
