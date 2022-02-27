package cz.cvut.fit.hanespat.service;

import cz.cvut.fit.hanespat.business.services.DeliveryBoyService;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryBoyCreateDTO;
import cz.cvut.fit.hanespat.data.dto.DeliveryBoyDTO;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.entity.DeliveryBoy;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryBoyRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class DeliveryBoyServiceTest {

    @Autowired
    private DeliveryBoyService deliveryBoyService;

    @MockBean
    private DeliveryBoyRepository deliveryBoyRepository;

    @MockBean
    private DeliveryAreaRepository deliveryAreaRepository;


    @Test
    void create () {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", List.of( deliveryArea ) );
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( deliveryBoy.getId(), deliveryBoy.getName(), deliveryBoy.getSurname() ,deliveryBoy.getPhoneNumber(), List.of( deliveryArea.getId() ) );
        DeliveryBoyCreateDTO deliveryBoyCreateDTO = new DeliveryBoyCreateDTO( deliveryBoy.getName(), deliveryBoy.getSurname(), deliveryBoy.getPhoneNumber(), List.of( deliveryArea.getId() ) );

        // Case: Area ids are found, new deliveryBoy is created and saved, method returns DTO of that new deliveryBoy
        BDDMockito.given( deliveryAreaRepository.findAllById( deliveryBoyCreateDTO.getDeliveryAreaIds() ) ).willReturn( List.of( deliveryArea ) );
        BDDMockito.given( deliveryBoyRepository.save( Mockito.any() ) ).willReturn( deliveryBoy );
        Optional<DeliveryBoyDTO> deliveryBoyReturn = deliveryBoyService.create( deliveryBoyCreateDTO );
        Assertions.assertTrue( compareDTO( deliveryBoyDTO, deliveryBoyReturn.get() ) );

        // Case: Area ids numbers dont match up, will return empty Optional
        BDDMockito.given( deliveryAreaRepository.findAllById( deliveryBoyCreateDTO.getDeliveryAreaIds() ) ).willReturn( Collections.emptyList() );
        deliveryBoyReturn = deliveryBoyService.create( deliveryBoyCreateDTO );
        Assertions.assertTrue(  deliveryBoyReturn.isEmpty()  );
    }


    @Test
    void update () {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", List.of( deliveryArea ) );
        DeliveryBoyCreateDTO deliveryBoyUpdated = new DeliveryBoyCreateDTO("New Test name", "New Test surname", "New Test phone number", List.of( deliveryArea.getId() ) );
        DeliveryBoyDTO expectedDeliveryBoy = new DeliveryBoyDTO( deliveryBoy.getId(), deliveryBoyUpdated.getName(), deliveryBoyUpdated.getSurname(), deliveryBoyUpdated.getPhoneNumber(), List.of( deliveryArea.getId() ) );

        // Case: Delivery boy is found, delivery areas are found, returns the updated delivery boy DTO
        BDDMockito.given( deliveryAreaRepository.findAllById( deliveryBoyUpdated.getDeliveryAreaIds() ) ).willReturn( List.of( deliveryArea ) );
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.of( deliveryBoy ) );
        Optional<DeliveryBoyDTO> deliveryBoyReturn = deliveryBoyService.update( deliveryBoy.getId(), deliveryBoyUpdated );
        Assertions.assertTrue( compareDTO( expectedDeliveryBoy, deliveryBoyReturn.get() ) );

        // Case: Delivery boy is not found, returns Optional empty
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.empty() );
        deliveryBoyReturn = deliveryBoyService.update( deliveryBoy.getId(), deliveryBoyUpdated );
        Assertions.assertTrue(  deliveryBoyReturn.isEmpty()  );

        // Case: Delivery areas are not found, returns Optional empty
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.of( deliveryBoy ) );
        BDDMockito.given( deliveryAreaRepository.findAllById( deliveryBoyUpdated.getDeliveryAreaIds() ) ).willReturn( Collections.emptyList() );
        deliveryBoyReturn = deliveryBoyService.update( deliveryBoy.getId(), deliveryBoyUpdated );
        Assertions.assertTrue(  deliveryBoyReturn.isEmpty()  );
    }


    @Test
    void findById () {
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", Collections.emptyList() );
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( deliveryBoy.getId(), deliveryBoy.getName(), deliveryBoy.getSurname() ,deliveryBoy.getPhoneNumber(), Collections.emptyList() );

        // Case: Delivery boy is found, returns DTO of the delivery boy
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.of( deliveryBoy ));
        Optional<DeliveryBoyDTO> deliveryBoyReturn = deliveryBoyService.findById( deliveryBoy.getId() );
        Assertions.assertTrue( compareDTO( deliveryBoyDTO, deliveryBoyReturn.get() ) );

        // Case: delivery boy is not found, returns empty optional
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.empty() );
        deliveryBoyReturn = deliveryBoyService.findById( deliveryBoy.getId() );
        Assertions.assertTrue( deliveryBoyReturn.isEmpty() );
    }

    @Test
    void findAll () {
        // Create array of 3 objects, pass to method, and expect array of 3 objects in DTO
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        List<DeliveryBoy> list = new ArrayList<DeliveryBoy>();
        List<DeliveryBoyDTO> listDTO = new ArrayList<DeliveryBoyDTO>();;
        for ( int i = 0; i < 3; i++ ) {
            DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", List.of( deliveryArea ) );
            list.add( deliveryBoy );
            listDTO.add( new DeliveryBoyDTO( deliveryBoy.getId(),deliveryBoy.getName(), deliveryBoy.getSurname(), deliveryBoy.getPhoneNumber(), List.of( deliveryArea.getId() ) ) );
        }
        BDDMockito.given( deliveryBoyRepository.findAllByDeliveryAreaName( deliveryArea.getName() ) ).willReturn( list );
        List<DeliveryBoyDTO> listReturn = deliveryBoyService.findAllByDeliveryAreaName( deliveryArea.getName() );
        for ( int i = 0; i < 3; i++ ) {
            Assertions.assertTrue( compareDTO( listDTO.get(i), listReturn.get(i) ) );
        }

    }

    @Test
    void findAllByDeliveryAreaName () {
        // Create array of 3 objects, pass to method, and expect array of 3 objects in DTO
        List<DeliveryBoy> list = new ArrayList<DeliveryBoy>();
        List<DeliveryBoyDTO> listDTO = new ArrayList<DeliveryBoyDTO>();;
        for ( int i = 0; i < 3; i++ ) {
            DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", Collections.emptyList() );
            list.add( deliveryBoy );
            listDTO.add( new DeliveryBoyDTO( deliveryBoy.getId(),deliveryBoy.getName(), deliveryBoy.getSurname(), deliveryBoy.getPhoneNumber(), Collections.emptyList() ) );
        }
        BDDMockito.given( deliveryBoyRepository.findAll() ).willReturn( list );
        List<DeliveryBoyDTO> listReturn = deliveryBoyService.findAll();
        for ( int i = 0; i < 3; i++ ) {
            Assertions.assertTrue( compareDTO( listDTO.get(i), listReturn.get(i) ) );
        }
    }


    @Test
    void deleteById () {
        DeliveryBoy deliveryBoy = new DeliveryBoy( "Test name", "Test surname", "Test phone number", Collections.emptyList() );
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( deliveryBoy.getId(), deliveryBoy.getName(), deliveryBoy.getSurname() ,deliveryBoy.getPhoneNumber(), Collections.emptyList() );

        // Case: Delivery boy is found and deleted, returns DTO of the delivery boy
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.of( deliveryBoy ));
        Optional<DeliveryBoyDTO> deliveryBoyReturn = deliveryBoyService.deleteById( deliveryBoy.getId() );
        Assertions.assertTrue( compareDTO( deliveryBoyDTO, deliveryBoyReturn.get() ) );
        Mockito.verify( deliveryBoyRepository, Mockito.atLeastOnce() ).deleteById( deliveryBoy.getId() );

        // Case: Delivery boy is not found, will return an empty Optional
        BDDMockito.given( deliveryBoyRepository.findById( deliveryBoy.getId() ) ).willReturn( Optional.empty() );
        deliveryBoyReturn = deliveryBoyService.deleteById( deliveryBoy.getId() );
        Assertions.assertTrue( deliveryBoyReturn.isEmpty() );
    }


    // Private method used for comparing DTO delivery boy objects
    private boolean compareDTO ( DeliveryBoyDTO a, DeliveryBoyDTO b ) {
        return ( ( a.getId() == b.getId() ) &&
                ( a.getName().equals(b.getName()) ) &&
                ( a.getSurname().equals(b.getSurname()) ) &&
                ( a.getPhoneNumber().equals(b.getPhoneNumber())) &&
                a.getDeliveryAreaIds().equals( b.getDeliveryAreaIds() )) ;
    }


}
