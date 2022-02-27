package cz.cvut.fit.hanespat.business.services;

import cz.cvut.fit.hanespat.data.dto.DeliveryBoyCreateDTO;
import cz.cvut.fit.hanespat.data.dto.DeliveryBoyDTO;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryBoyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryBoyService {

    private final DeliveryBoyRepository deliveryBoyRepository;
    private final DeliveryAreaRepository deliveryAreaRepository;

    @Autowired
    public DeliveryBoyService(DeliveryBoyRepository deliveryBoyRepository, DeliveryAreaRepository deliveryAreaRepository) {
        this.deliveryBoyRepository = deliveryBoyRepository;
        this.deliveryAreaRepository = deliveryAreaRepository;
    }

    @Transactional
    public Optional<DeliveryBoyDTO> create ( DeliveryBoyCreateDTO deliveryBoyDTO ) {

        // Get delivery areas, if the numbers dont match, return empty and controller will throw an error
        List<DeliveryArea> deliveryAreas = deliveryAreaRepository.findAllById( deliveryBoyDTO.getDeliveryAreaIds() );
        if ( deliveryAreas.size() != deliveryBoyDTO.getDeliveryAreaIds().size() )
            return Optional.empty();

        return Optional.of(toDTO ( deliveryBoyRepository.save (
                new DeliveryBoy(
                        deliveryBoyDTO.getName(),
                        deliveryBoyDTO.getSurname(),
                        deliveryBoyDTO.getPhoneNumber(),
                        deliveryAreas
                ) ) ) );
    }

    public Optional<DeliveryBoyDTO> findById ( int id ) {
        return toDTO( deliveryBoyRepository.findById(id) );
    }

    public List<DeliveryBoyDTO> findAll() {
        return deliveryBoyRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<DeliveryBoyDTO> findAllByDeliveryAreaName ( String name ) {
        return deliveryBoyRepository.findAllByDeliveryAreaName( name ).stream().map(this::toDTO).collect(Collectors.toList());
    }


    @Transactional
    public Optional<DeliveryBoyDTO> update ( int id, DeliveryBoyCreateDTO deliveryBoyDTO ) {

        // Try to find delivery boy by id
        Optional<DeliveryBoy> optionalDeliveryBoy = deliveryBoyRepository.findById( id );

        // If delivery boy wasn't found, just return and controller will throw an error
        if ( optionalDeliveryBoy.isEmpty() )
            return toDTO( optionalDeliveryBoy );

        // Try to find delivery areas, if the numbers dont match, just return and controller with throw error
        List<DeliveryArea> deliveryAreas = deliveryAreaRepository.findAllById( deliveryBoyDTO.getDeliveryAreaIds() );
        if ( deliveryAreas.size() != deliveryBoyDTO.getDeliveryAreaIds().size() )
            return Optional.empty();

        // Update the found delivery boy
        optionalDeliveryBoy.get().setName( deliveryBoyDTO.getName() );
        optionalDeliveryBoy.get().setSurname( deliveryBoyDTO.getSurname() );
        optionalDeliveryBoy.get().setPhoneNumber( deliveryBoyDTO.getPhoneNumber() );
        optionalDeliveryBoy.get().setDeliveryAreas( deliveryAreas );
        return toDTO( optionalDeliveryBoy );
    }

    @Transactional
    public Optional<DeliveryBoyDTO> deleteById (int id ) {

        // First find the delivery boy, if he is found, delete him by id
        Optional<DeliveryBoy> optionalDeliveryBoy = deliveryBoyRepository.findById( id );
        if ( !optionalDeliveryBoy.isEmpty() )
            deliveryBoyRepository.deleteById(id);

        return toDTO( optionalDeliveryBoy );
    }


    private DeliveryBoyDTO toDTO ( DeliveryBoy deliveryBoy ) {
        return new DeliveryBoyDTO (
                deliveryBoy.getId(),
                deliveryBoy.getName(),
                deliveryBoy.getSurname(),
                deliveryBoy.getPhoneNumber(),
                deliveryBoy.getDeliveryAreas().stream().map( DeliveryArea::getId ).collect( Collectors.toList()) );
    }

    private Optional<DeliveryBoyDTO> toDTO ( Optional<DeliveryBoy> deliveryBoy ) {
        if ( deliveryBoy.isEmpty() )
            return Optional.empty();
        return Optional.of( toDTO( deliveryBoy.get() ) );
    }

}
