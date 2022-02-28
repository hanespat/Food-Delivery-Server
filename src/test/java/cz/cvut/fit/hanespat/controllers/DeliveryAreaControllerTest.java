package cz.cvut.fit.hanespat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.hanespat.data.dto.DeliveryAreaDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryAreaCreateDTO;
import cz.cvut.fit.hanespat.services.DeliveryAreaService;
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
class DeliveryAreaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DeliveryAreaService deliveryAreaService;

    @Test
    void create() throws Exception {
        DeliveryAreaCreateDTO deliveryAreaCreateDTO = new DeliveryAreaCreateDTO( "Test name", Collections.emptyList() );
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( 1,deliveryAreaCreateDTO.name(), deliveryAreaCreateDTO.deliveryBoyIds() );

        // Case: Delivery area is created, status OK and DTO of the delivery area is returned
        BDDMockito.given( deliveryAreaService.create( Mockito.any()) ).willReturn( Optional.of(deliveryAreaDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/delivery_area")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryAreaDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryAreaDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryBoyIds", CoreMatchers.is( deliveryAreaDTO.deliveryBoyIds() )));
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).create( Mockito.any() );

        // Case: Delivery area is not created, status NOT FOUND
        BDDMockito.given( deliveryAreaService.create( Mockito.any()) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/delivery_area")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).create( Mockito.any() );
    }

    @Test
    void findById() throws Exception {
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( 1,"Test name", Collections.emptyList() );

        // Case: Delivery area is deleted, status OK and DTO of the deleted delivery area is returned
        BDDMockito.given( deliveryAreaService.findById( Mockito.any( Integer.class )) ).willReturn( Optional.of(deliveryAreaDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_area/{id}", deliveryAreaDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryAreaDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryAreaDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryBoyIds", CoreMatchers.is( deliveryAreaDTO.deliveryBoyIds() )));
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );

        // Case: Delivery area is not deleted, status NOT FOUND
        BDDMockito.given( deliveryAreaService.findById( Mockito.any( Integer.class )) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_area/{id}", deliveryAreaDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );
    }

    @Test
    void findAll() throws Exception {
        // Create list of two delivery areas and expect to get a list of json objects of these delivery areas
        DeliveryAreaDTO deliveryAreaDTO_A = new DeliveryAreaDTO( 1,"Test name A", Collections.emptyList() );
        DeliveryAreaDTO deliveryAreaDTO_B = new DeliveryAreaDTO( 2,"Test name B", Collections.emptyList() );

        // Case: Get delivery areas
        BDDMockito.given( deliveryAreaService.findAll()).willReturn( List.of( deliveryAreaDTO_A, deliveryAreaDTO_B ) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_area/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].id", Matchers.containsInAnyOrder( deliveryAreaDTO_A.id(), deliveryAreaDTO_B.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder( deliveryAreaDTO_A.name(), deliveryAreaDTO_B.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].deliveryBoyIds", Matchers.containsInAnyOrder( deliveryAreaDTO_A.deliveryBoyIds(), deliveryAreaDTO_B.deliveryBoyIds() )));
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).findAll();
    }

    @Test
    void update() throws Exception {
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( 1,"Test name", Collections.emptyList() );
        DeliveryAreaCreateDTO deliveryAreaCreateDTO = new DeliveryAreaCreateDTO( "New test name", deliveryAreaDTO.deliveryBoyIds() );
        DeliveryAreaDTO updatedDTO = new DeliveryAreaDTO( deliveryAreaDTO.id(), deliveryAreaCreateDTO.name(), deliveryAreaCreateDTO.deliveryBoyIds() );

        // Case: Delivery area is updated, status OK and DTO of updated delivery area is returned
        BDDMockito.given( deliveryAreaService.update( Mockito.any( Integer.class ), Mockito.any( DeliveryAreaCreateDTO.class ) ) ).willReturn( Optional.of(updatedDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/delivery_area/{id}", deliveryAreaDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( updatedDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( updatedDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryBoyIds", CoreMatchers.is( updatedDTO.deliveryBoyIds() )));
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( DeliveryAreaCreateDTO.class ) );


        // Case: Delivery area is not updated, status NOT FOUND
        BDDMockito.given( deliveryAreaService.update( Mockito.any( Integer.class ), Mockito.any( DeliveryAreaCreateDTO.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/delivery_area/{id}", deliveryAreaDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( DeliveryAreaCreateDTO.class ) );
    }


    @Test
    void deleteById() throws Exception {
        DeliveryAreaDTO deliveryAreaDTO = new DeliveryAreaDTO( 1,"Test name", Collections.emptyList() );

        // Case: Delivery area is deleted, status OK and DTO of the deleted delivery area is returned
        BDDMockito.given( deliveryAreaService.deleteById( Mockito.any( Integer.class )) ).willReturn( Optional.of(deliveryAreaDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/delivery_area/{id}", deliveryAreaDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryAreaDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryAreaDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryBoyIds", CoreMatchers.is( deliveryAreaDTO.deliveryBoyIds() )));
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );

        // Case: Delivery area is not deleted, status NOT FOUND
        BDDMockito.given( deliveryAreaService.deleteById( Mockito.any( Integer.class )) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/delivery_area/{id}", deliveryAreaDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryAreaDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryAreaService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );
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