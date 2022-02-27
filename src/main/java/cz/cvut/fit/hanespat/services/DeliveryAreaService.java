package cz.cvut.fit.hanespat.services;

import cz.cvut.fit.hanespat.data.dto.DeliveryAreaDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryAreaCreateDTO;
import cz.cvut.fit.hanespat.data.entity.Address;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import cz.cvut.fit.hanespat.data.repository.AddressRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryBoyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryAreaService {

    private final DeliveryAreaRepository deliveryAreaRepository;
    private final DeliveryBoyRepository deliveryBoyRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public DeliveryAreaService(DeliveryAreaRepository deliveryAreaRepository, DeliveryBoyRepository deliveryBoyRepository, AddressRepository addressRepository) {
        this.deliveryAreaRepository = deliveryAreaRepository;
        this.deliveryBoyRepository = deliveryBoyRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Optional<DeliveryAreaDTO> create (DeliveryAreaCreateDTO deliveryAreaDTO ) {

        // Get delivery boys, if the numbers dont match, just return empty and controller will throw an error
        List<DeliveryBoy> deliveryBoys = deliveryBoyRepository.findAllById( deliveryAreaDTO.getDeliveryBoyIds() );
        if ( deliveryBoys.size() != deliveryAreaDTO.getDeliveryBoyIds().size() )
            return Optional.empty();

        DeliveryArea deliveryArea = new DeliveryArea( deliveryAreaDTO.getName(), deliveryBoys );
        return Optional.of( toDTO( deliveryAreaRepository.save( deliveryArea ) ) );

    }

    @Transactional
    public Optional<DeliveryAreaDTO> update ( int id, DeliveryAreaCreateDTO deliveryAreaDTO ) {

        // Try to find delivery area by id
        Optional<DeliveryArea> optionalDeliveryArea = deliveryAreaRepository.findById( id );

        // If delivery area wasn't found, just return and controller will throw an error
        if ( optionalDeliveryArea.isEmpty() )
            return Optional.empty();

        // Get delivery boys, if the numbers dont match, just return empty and controller will throw an error
        List<DeliveryBoy> deliveryBoys = deliveryBoyRepository.findAllById( deliveryAreaDTO.getDeliveryBoyIds() );
        if ( deliveryBoys.size() != deliveryAreaDTO.getDeliveryBoyIds().size() )
            return Optional.empty();
        System.out.println( deliveryBoys.size() );

        // Update the found delivery area
        optionalDeliveryArea.get().setName( deliveryAreaDTO.getName() );
        optionalDeliveryArea.get().setDeliveryBoys( deliveryBoys );
        return toDTO( optionalDeliveryArea );
    }

    public Optional<DeliveryAreaDTO> findById ( int id ) {
        return toDTO( deliveryAreaRepository.findById(id) );
    }

    public List<DeliveryAreaDTO> findAll() {
        return deliveryAreaRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public Optional<DeliveryAreaDTO> deleteById ( int id ) {

        // Find the delivery area, if it is found, delete it by id, else just return empty and controller will throw an error
        Optional<DeliveryArea> optionalDeliveryArea = deliveryAreaRepository.findById( id );

        if ( !optionalDeliveryArea.isEmpty() ) {

            // First delete all addresses that reference the area, then delete the area
            for ( Address address: addressRepository.findAllByDeliveryAreaId( optionalDeliveryArea.get().getId() )  ) {
                addressRepository.deleteById( address.getId() );
            }
            deliveryAreaRepository.deleteById(id);
        }
        return toDTO( optionalDeliveryArea );
    }

    private DeliveryAreaDTO toDTO ( DeliveryArea deliveryArea ) {
        return new DeliveryAreaDTO (
                deliveryArea.getId(),
                deliveryArea.getName(),
                deliveryArea.getDeliveryBoys().stream().map( DeliveryBoy::getId ).collect( Collectors.toList()) );
    }

    private Optional<DeliveryAreaDTO> toDTO ( Optional<DeliveryArea> deliveryArea ) {
        if ( deliveryArea.isEmpty() )
            return Optional.empty();
        return Optional.of( toDTO ( deliveryArea.get() ) );
    }
}

