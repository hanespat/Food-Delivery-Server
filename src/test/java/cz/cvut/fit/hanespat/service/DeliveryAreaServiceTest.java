package cz.cvut.fit.hanespat.service;

import cz.cvut.fit.hanespat.services.DeliveryAreaService;
import cz.cvut.fit.hanespat.data.dto.DeliveryAreaDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryAreaCreateDTO;
import cz.cvut.fit.hanespat.data.entity.Address;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import cz.cvut.fit.hanespat.data.repository.AddressRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryBoyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
class DeliveryAreaServiceTest {

    @Autowired
    private DeliveryAreaService deliveryAreaService;

    @MockBean
    private DeliveryBoyRepository deliveryBoyRepository;

    @MockBean
    private DeliveryAreaRepository deliveryAreaRepository;

    @MockBean
    private AddressRepository addressRepository;

    @Test
    void create() {
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", Collections.emptyList() );
        DeliveryArea deliveryArea = new DeliveryArea( "Test name",List.of( deliveryBoy ) );
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( deliveryArea.getId(), deliveryArea.getName(), List.of( deliveryBoy.getId() ) );
        DeliveryAreaCreateDTO deliveryAreaCreateDTO = new DeliveryAreaCreateDTO( deliveryArea.getName(), List.of( deliveryBoy.getId() ) );

        // Case: Delivery boy ids are found, new Delivery Area is created and saved, method returns DTO of that new deliveryArea
        BDDMockito.given( deliveryBoyRepository.findAllById( deliveryAreaCreateDTO.getDeliveryBoyIds() ) ).willReturn( List.of( deliveryBoy ) );
        BDDMockito.given( deliveryAreaRepository.save( Mockito.any() ) ).willReturn( deliveryArea );
        Optional<DeliveryAreaDTO> deliveryAreaReturn = deliveryAreaService.create( deliveryAreaCreateDTO );
        Assertions.assertTrue( compareDTO( deliveryAreaDTO, deliveryAreaReturn.get() ) );

        // Case: DB ids numbers dont match up, will return empty Optional
        BDDMockito.given( deliveryBoyRepository.findAllById( deliveryAreaCreateDTO.getDeliveryBoyIds() ) ).willReturn( Collections.emptyList() );
        deliveryAreaReturn = deliveryAreaService.create( deliveryAreaCreateDTO );
        Assertions.assertTrue(  deliveryAreaReturn.isEmpty()  );
    }

    @Test
    void update() {
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", Collections.emptyList() );
        DeliveryArea deliveryArea = new DeliveryArea( "Test name",List.of( deliveryBoy ) );
        DeliveryAreaCreateDTO deliveryAreaUpdated = new DeliveryAreaCreateDTO("New Test name", List.of( deliveryBoy.getId() ) );
        DeliveryAreaDTO expectedDeliveryArea = new DeliveryAreaDTO( deliveryArea.getId(), deliveryAreaUpdated.getName(), deliveryAreaUpdated.getDeliveryBoyIds() );

        // Case: Delivery Area is found, delivery boys are found, returns the updated delivery area DTO
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of( deliveryArea ) );
        BDDMockito.given( deliveryBoyRepository.findAllById( deliveryAreaUpdated.getDeliveryBoyIds() ) ).willReturn( List.of( deliveryBoy ) );
        Optional<DeliveryAreaDTO> deliveryAreaReturn = deliveryAreaService.update( deliveryArea.getId(), deliveryAreaUpdated );
        Assertions.assertTrue( compareDTO( expectedDeliveryArea, deliveryAreaReturn.get() ) );

        // Case: Delivery Area is not found, returns Optional empty
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.empty() );
        deliveryAreaReturn = deliveryAreaService.update( deliveryArea.getId(), deliveryAreaUpdated );
        Assertions.assertTrue(  deliveryAreaReturn.isEmpty()  );

        // Case: Delivery boys are not found, returns Optional empty
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of( deliveryArea ) );
        BDDMockito.given( deliveryBoyRepository.findAllById( deliveryAreaUpdated.getDeliveryBoyIds() ) ).willReturn( Collections.emptyList() );
        deliveryAreaReturn = deliveryAreaService.update( deliveryArea.getId(), deliveryAreaUpdated );
        Assertions.assertTrue(  deliveryAreaReturn.isEmpty()  );

    }

    @Test
    void findById() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( deliveryArea.getId(), deliveryArea.getName(), Collections.emptyList() );

        // Case: Delivery Area is found, returns DTO of the delivery area
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of( deliveryArea ));
        Optional<DeliveryAreaDTO> deliveryAreaReturn = deliveryAreaService.findById( deliveryArea.getId() );
        Assertions.assertTrue( compareDTO( deliveryAreaDTO, deliveryAreaReturn.get() ) );

        // Case: Delivery Area is not found, returns empty optional
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.empty() );
        deliveryAreaReturn = deliveryAreaService.findById( deliveryArea.getId() );
        Assertions.assertTrue( deliveryAreaReturn.isEmpty() );
    }

    @Test
    void findAll() {
        // Create array of 3 objects, pass to method, and expect array of 3 objects in DTO
        List<DeliveryArea> list = new ArrayList<DeliveryArea>();
        List<DeliveryAreaDTO> listDTO = new ArrayList<DeliveryAreaDTO>();;
        for ( int i = 0; i < 3; i++ ) {
            DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
            list.add( deliveryArea );
            listDTO.add( new DeliveryAreaDTO( deliveryArea.getId(),deliveryArea.getName(), Collections.emptyList() ) );
        }
        BDDMockito.given( deliveryAreaRepository.findAll() ).willReturn( list );
        List<DeliveryAreaDTO> listReturn = deliveryAreaService.findAll();
        for ( int i = 0; i < 3; i++ ) {
            Assertions.assertTrue( compareDTO( listDTO.get(i), listReturn.get(i) ) );
        }

    }

    @Test
    void deleteById() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        Address address = new Address( 2, "Test street", "Test zipcode", deliveryArea  );
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( deliveryArea.getId(), deliveryArea.getName(), Collections.emptyList() );

        // Case: Delivery Area is found , deletes all addresses that reference it, deletes the area itself, returns DTO of the delivery area
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of( deliveryArea ));
        BDDMockito.given( addressRepository.findAllByDeliveryAreaId( deliveryArea.getId() )).willReturn( List.of( address ) );

        Optional<DeliveryAreaDTO> deliveryAreaReturn = deliveryAreaService.deleteById( deliveryArea.getId() );
        Assertions.assertTrue( compareDTO( deliveryAreaDTO, deliveryAreaReturn.get() ) );

        Mockito.verify( deliveryAreaRepository, Mockito.atLeastOnce() ).deleteById( deliveryArea.getId() );
        Mockito.verify( addressRepository, Mockito.atLeastOnce() ).findAllByDeliveryAreaId( deliveryArea.getId() );
        Mockito.verify( addressRepository, Mockito.atLeastOnce() ).deleteById( address.getId() );

        // Case: Delivery Area is not found, will return an empty Optional
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.empty() );
        deliveryAreaReturn = deliveryAreaService.deleteById( deliveryArea.getId() );
        Assertions.assertTrue( deliveryAreaReturn.isEmpty() );
    }


    // Private method used for comparing DTO delivery area objects
    private boolean compareDTO ( DeliveryAreaDTO a, DeliveryAreaDTO b ) {
        return (( a.getId() == b.getId() ) &&
                ( a.getName().equals(b.getName()) ) &&
                ( a.getDeliveryBoyIds().equals( b.getDeliveryBoyIds() ) ) ) ;
    }
}
