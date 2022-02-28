package cz.cvut.fit.hanespat.service;

import cz.cvut.fit.hanespat.services.AddressService;
import cz.cvut.fit.hanespat.data.dto.AddressDTO;
import cz.cvut.fit.hanespat.data.dto.create.AddressCreateDTO;
import cz.cvut.fit.hanespat.data.entity.Address;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.data.repository.AddressRepository;
import cz.cvut.fit.hanespat.data.repository.DeliveryAreaRepository;
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
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private DeliveryAreaRepository deliveryAreaRepository;

    @Test
    void create() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        Address address = new Address( 2, "Test street", "Test zipcode", deliveryArea  );
        AddressDTO addressDTO = new AddressDTO( address.getId(), address.getHouseNumber(), address.getStreet(), address.getZipcode(), deliveryArea.getId() );
        AddressCreateDTO addressCreateDTO = new AddressCreateDTO( address.getHouseNumber(), address.getStreet(), address.getZipcode(), deliveryArea.getId() );

        // Case: Delivery Area is found, new Address is created and saved, method returns DTO of that new address
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of(deliveryArea) );
        BDDMockito.given( addressRepository.save( Mockito.any() ) ).willReturn( address );
        Optional<AddressDTO> addressReturn = addressService.create( addressCreateDTO );
        Assertions.assertTrue( compareDTO( addressDTO, addressReturn.get() ) );

        // Case: Delivery area id is not found, will return empty Optional
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.empty() );
        addressReturn = addressService.create( addressCreateDTO );
        Assertions.assertTrue(  addressReturn.isEmpty()  );
    }

    @Test
    void update() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        Address address = new Address( 2, "Test street", "Test zipcode", deliveryArea  );
        AddressCreateDTO addressUpdatedDTO = new AddressCreateDTO( 3, "New Test street", "New Test zipcode", deliveryArea.getId() );
        AddressDTO expectedAddress = new AddressDTO( address.getId(), addressUpdatedDTO.getHouseNumber(), addressUpdatedDTO.getStreet(), addressUpdatedDTO.getZipCode(), addressUpdatedDTO.getDeliveryAreaId() );

        // Case: Delivery Area is found, address is found, returns the updated address DTO
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.of( address ) );
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.of( deliveryArea ) );
        Optional<AddressDTO> addressReturn = addressService.update( address.getId(), addressUpdatedDTO );
        Assertions.assertTrue( compareDTO( expectedAddress, addressReturn.get() ) );

        // Case: Address is not found, returns Optional empty
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.empty() );
        addressReturn = addressService.update( address.getId(), addressUpdatedDTO );
        Assertions.assertTrue(  addressReturn.isEmpty()  );

        // Case: Delivery area is not found, returns Optional empty
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.of( address ) );
        BDDMockito.given( deliveryAreaRepository.findById( deliveryArea.getId() ) ).willReturn( Optional.empty() );
        addressReturn = addressService.update( address.getId(), addressUpdatedDTO );
        Assertions.assertTrue(  addressReturn.isEmpty()  );
    }

    @Test
    void findById() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        Address address = new Address( 2, "Test street", "Test zipcode", deliveryArea  );
        AddressDTO addressDTO = new AddressDTO( address.getId(), address.getHouseNumber(), address.getStreet(), address.getZipcode(), deliveryArea.getId() );

        // Case: Address is found, returns DTO of the address
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.of( address ));
        Optional<AddressDTO> addressReturn = addressService.findById( address.getId() );
        Assertions.assertTrue( compareDTO( addressDTO, addressReturn.get() ) );

        // Case: Address is not found, will return an empty Optional
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.empty() );
        addressReturn = addressService.findById( address.getId() );
        Assertions.assertTrue( addressReturn.isEmpty() );
    }

    @Test
    void findAll() {
        // Create array of 3 objects, pass to method, and expect array of 3 objects in DTO
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        List<Address> list = new ArrayList<Address>();
        List<AddressDTO> listDTO = new ArrayList<AddressDTO>();;
        for ( int i = 0; i < 3; i++ ) {
            Address address = new Address( i, "Test street", "Test zipcode", deliveryArea );
            list.add( address );
            listDTO.add( new AddressDTO( address.getId(), address.getHouseNumber(), address.getStreet(), address.getZipcode(), deliveryArea.getId() ) );
        }
        BDDMockito.given( addressRepository.findAll() ).willReturn( list );
        List<AddressDTO> listReturn = addressService.findAll();
        for ( int i = 0; i < 3; i++ ) {
            Assertions.assertTrue( compareDTO( listDTO.get(i), listReturn.get(i) ) );
        }
    }

    @Test
    void deleteById() {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        Address address = new Address( 2, "Test street", "Test zipcode", deliveryArea  );
        AddressDTO addressDTO = new AddressDTO( address.getId(), address.getHouseNumber(), address.getStreet(), address.getZipcode(), deliveryArea.getId() );

        // Case: Address is found and deleted, returns DTO of the address
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.of( address ));
        Optional<AddressDTO> addressReturn = addressService.deleteById( address.getId() );
        Assertions.assertTrue( compareDTO( addressDTO, addressReturn.get() ) );
        Mockito.verify( addressRepository, Mockito.atLeastOnce() ).deleteById( address.getId() );

        // Case: Address is not found, will return an empty Optional
        BDDMockito.given( addressRepository.findById( address.getId() ) ).willReturn( Optional.empty() );
        addressReturn = addressService.deleteById( address.getId() );
        Assertions.assertTrue( addressReturn.isEmpty() );
    }


    // Private method used for comparing DTO address objects
    private boolean compareDTO ( AddressDTO a, AddressDTO b ) {
        return (( a.getId() == b.getId() ) &&
                ( a.getHouseNumber() == b.getHouseNumber() ) &&
                ( a.getStreet().equals( b.getStreet() ) ) &&
                ( a.getZipCode().equals( b.getZipCode() )) &&
                (a.getDeliveryAreaId() == b.getDeliveryAreaId() ) ) ;
    }
}

