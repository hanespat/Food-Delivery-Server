package cz.cvut.fit.hanespat.data.dto;

import java.util.List;

public record DeliveryBoyDTO ( int id, String name, String surname, String phoneNumber, List<Integer> deliveryAreaIds ) {}

