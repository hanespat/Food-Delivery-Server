package cz.cvut.fit.hanespat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.hanespat.data.dto.AddressDTO;
import cz.cvut.fit.hanespat.data.dto.create.AddressCreateDTO;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.services.AddressService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class AddressControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    void create() throws Exception {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        AddressCreateDTO addressCreateDTO = new AddressCreateDTO( 24, "Test street","Test ", deliveryArea.getId() );
        AddressDTO addressDTO = new AddressDTO( 1,addressCreateDTO.houseNumber(), addressCreateDTO.street(), addressCreateDTO.zipCode(), addressCreateDTO.deliveryAreaId() );

        // Case: Address is created, status OK and DTO of the address is returned
        BDDMockito.given( addressService.create( Mockito.any()) ).willReturn( Optional.of(addressDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( addressDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.houseNumber", CoreMatchers.is( addressDTO.houseNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is( addressDTO.street() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.zipCode", CoreMatchers.is( addressDTO.zipCode() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaId", CoreMatchers.is( addressDTO.deliveryAreaId() )));
        Mockito.verify( addressService, Mockito.atLeastOnce()).create( Mockito.any() );

        // Case: Address is not created, status NOT FOUND
        BDDMockito.given( addressService.create( Mockito.any()) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( addressService, Mockito.atLeastOnce()).create( Mockito.any() );
    }

    @Test
    void update() throws Exception {

        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        AddressDTO addressDTO = new AddressDTO( 1,24, "Test street","Test zipcode", deliveryArea.getId() );
        AddressCreateDTO addressCreateDTO = new AddressCreateDTO( 25, "Test new street","Test new", deliveryArea.getId() );
        AddressDTO updatedDTO = new AddressDTO( addressDTO.id(), addressCreateDTO.houseNumber(), addressCreateDTO.street(), addressCreateDTO.zipCode(), addressCreateDTO.deliveryAreaId() );

        // Case: Address is updated, status OK and DTO of updated address is returned
        BDDMockito.given( addressService.update( Mockito.any( Integer.class ), Mockito.any( AddressCreateDTO.class ) ) ).willReturn( Optional.of(updatedDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/address/{id}", addressDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( updatedDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.houseNumber", CoreMatchers.is( updatedDTO.houseNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is( updatedDTO.street() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.zipCode", CoreMatchers.is( updatedDTO.zipCode() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaId", CoreMatchers.is( updatedDTO.deliveryAreaId() )));
        Mockito.verify( addressService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( AddressCreateDTO.class ) );


        // Case: Address is not updated, status NOT FOUND
        BDDMockito.given( addressService.update( Mockito.any( Integer.class ), Mockito.any( AddressCreateDTO.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/address/{id}", addressDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( addressService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( AddressCreateDTO.class ) );

    }

    @Test
    void findById() throws Exception {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        AddressDTO addressDTO = new AddressDTO( 1, 24, "Test street","Test ", deliveryArea.getId() );

        // Case: Address is found, status OK and DTO of the found address is returned
        BDDMockito.given( addressService.findById( Mockito.any( Integer.class ) ) ).willReturn( Optional.of(addressDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/address/{id}", addressDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( addressDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.houseNumber", CoreMatchers.is( addressDTO.houseNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is( addressDTO.street() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.zipCode", CoreMatchers.is( addressDTO.zipCode() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaId", CoreMatchers.is( addressDTO.deliveryAreaId() )));
        Mockito.verify( addressService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );


        // Case: Address is not found, status NOT FOUND
        BDDMockito.given( addressService.findById( Mockito.any( Integer.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/address/{id}", addressDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( addressService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );
    }

    @Test
    void findAll() throws Exception {

        // Create list of two addresses and expect to get a list of json objects of these addresses
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        AddressDTO addressDTO_A = new AddressDTO( 1, 24, "Test street","Test zipcode", deliveryArea.getId() );
        AddressDTO addressDTO_B = new AddressDTO( 2, 18, "Test street","Test zipcode", deliveryArea.getId() );


        // Case: Get addresses
        BDDMockito.given( addressService.findAll()).willReturn( List.of( addressDTO_A, addressDTO_B ) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/address/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].id", Matchers.containsInAnyOrder( addressDTO_A.id(), addressDTO_B.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].houseNumber", Matchers.containsInAnyOrder( addressDTO_A.houseNumber(), addressDTO_B.houseNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].street", Matchers.containsInAnyOrder( addressDTO_A.street(), addressDTO_B.street() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].zipCode", Matchers.containsInAnyOrder( addressDTO_A.zipCode(), addressDTO_B.zipCode() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].deliveryAreaId", CoreMatchers.hasItem( deliveryArea.getId() ) ));
        Mockito.verify( addressService, Mockito.atLeastOnce()).findAll();
    }


    @Test
    void deleteById() throws Exception {
        DeliveryArea deliveryArea = new DeliveryArea( "Test name", Collections.emptyList() );
        AddressDTO addressDTO = new AddressDTO( 1, 24, "Test street","Test ", deliveryArea.getId() );

        // Case: Address is deleted, status OK and DTO of the deleted address is returned
        BDDMockito.given( addressService.deleteById( Mockito.any( Integer.class ) ) ).willReturn( Optional.of(addressDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/address/{id}", addressDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( addressDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.houseNumber", CoreMatchers.is( addressDTO.houseNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.street", CoreMatchers.is( addressDTO.street() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.zipCode", CoreMatchers.is( addressDTO.zipCode() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaId", CoreMatchers.is( addressDTO.deliveryAreaId() )));
        Mockito.verify( addressService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );


        // Case: Address is not found, status NOT FOUND
        BDDMockito.given( addressService.deleteById( Mockito.any( Integer.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/address/{id}", addressDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( addressDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( addressService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );
    }

    private String toJsonString ( final Object object ) {
        try {
            return new ObjectMapper().writeValueAsString( object );
        }
        catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }
}

