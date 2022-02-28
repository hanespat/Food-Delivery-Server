package cz.cvut.fit.hanespat.data.dto;

import java.util.List;

public record DeliveryAreaDTO ( int id, String name, List<Integer> deliveryBoyIds ) {}
