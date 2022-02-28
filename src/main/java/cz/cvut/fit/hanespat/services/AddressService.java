package cz.cvut.fit.hanespat.services;

import cz.cvut.fit.hanespat.data.dto.AddressDTO;
import cz.cvut.fit.hanespat.data.dto.create.AddressCreateDTO;
import cz.cvut.fit.hanespat.data.entity.Address;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.repository.AddressRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final DeliveryAreaRepository deliveryAreaRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, DeliveryAreaRepository deliveryAreaRepository) {
        this.addressRepository = addressRepository;
        this.deliveryAreaRepository = deliveryAreaRepository;
    }

    @Transactional
    public Optional<AddressDTO> create(AddressCreateDTO addressDTO) {

        // First get delivery area by id, if the area is empty, throw exception
        Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findById(addressDTO.deliveryAreaId());
        if (deliveryArea.isEmpty())
            return Optional.empty();

        Address address = new Address(
                addressDTO.houseNumber(),
                addressDTO.street(),
                addressDTO.zipCode(),
                deliveryArea.get()
        );

        return Optional.of(toDTO(addressRepository.save(address)));
    }

    @Transactional
    public Optional<AddressDTO> update(int id, AddressCreateDTO addressDTO_entry) {

        // Try to find address by id
        Optional<Address> optionalAddress = addressRepository.findById(id);

        // If address wasn't found, just return and controller will throw an error
        if (optionalAddress.isEmpty())
            return Optional.empty();

        // Get delivery area by id, if the number of found area is not found, throw exception
        Optional<DeliveryArea> deliveryArea = deliveryAreaRepository.findById(addressDTO_entry.deliveryAreaId());
        if (deliveryArea.isEmpty())
            return Optional.empty();

        // Update the found address
        optionalAddress.get().setHouseNumber(addressDTO_entry.houseNumber());
        optionalAddress.get().setStreet(addressDTO_entry.street());
        optionalAddress.get().setZipcode(addressDTO_entry.zipCode());
        optionalAddress.get().setDeliveryArea(deliveryArea.get());
        return toDTO(optionalAddress);
    }

    public Optional<AddressDTO> findById(int id) {
        return toDTO(addressRepository.findById(id));
    }

    public List<AddressDTO> findAll() {
        return addressRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public Optional<AddressDTO> deleteById(int id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (!optionalAddress.isEmpty())
            addressRepository.deleteById(id);
        return toDTO(optionalAddress);
    }

    private AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getHouseNumber(),
                address.getStreet(),
                address.getZipcode(),
                address.getDeliveryArea().getId()
        );
    }

    private Optional<AddressDTO> toDTO(Optional<Address> address) {
        if (address.isEmpty())
            return Optional.empty();
        return Optional.of(toDTO(address.get()));
    }
}

