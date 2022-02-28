package cz.cvut.fit.hanespat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fit.hanespat.data.dto.DeliveryBoyDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryBoyCreateDTO;
import cz.cvut.fit.hanespat.data.entity.DeliveryArea;
import cz.cvut.fit.hanespat.services.DeliveryBoyService;
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
class DeliveryBoyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private DeliveryBoyService deliveryBoyService;

    @Test
    void create() throws Exception {

        DeliveryBoyCreateDTO deliveryBoyCreateDTO = new DeliveryBoyCreateDTO( "Test name", "Test surname","Test phoneNumber", Collections.emptyList() );
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( 1,deliveryBoyCreateDTO.name(), deliveryBoyCreateDTO.surname(), deliveryBoyCreateDTO.phoneNumber(), deliveryBoyCreateDTO.deliveryAreaIds() );

        // Case: Delivery boy is created, status OK and DTO of delivery boy is returned
        BDDMockito.given( deliveryBoyService.create( Mockito.any()) ).willReturn( Optional.of(deliveryBoyDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/delivery_boy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isCreated() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryBoyDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryBoyDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is( deliveryBoyDTO.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is( deliveryBoyDTO.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaIds", CoreMatchers.is( Collections.emptyList() )));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).create( Mockito.any() );

        // Case: Delivery boy is not created, status NOT FOUND
        BDDMockito.given( deliveryBoyService.create( Mockito.any()) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/delivery_boy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).create( Mockito.any() );
    }

    @Test
    void update() throws Exception {
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( 1, "Test name", "Test surname", "Test phone number", Collections.emptyList());
        DeliveryBoyCreateDTO deliveryBoyCreateDTO = new DeliveryBoyCreateDTO( "New test name", "New test surname", "new phone number", Collections.emptyList() );
        DeliveryBoyDTO updatedDTO = new DeliveryBoyDTO( deliveryBoyDTO.id(), deliveryBoyCreateDTO.name(), deliveryBoyCreateDTO.surname(), deliveryBoyCreateDTO.phoneNumber(), deliveryBoyCreateDTO.deliveryAreaIds() );

        // Case: Delivery boy is updated, status OK and DTO of updated delivery boy is returned
        BDDMockito.given( deliveryBoyService.update( Mockito.any( Integer.class ), Mockito.any( DeliveryBoyCreateDTO.class ) ) ).willReturn( Optional.of(updatedDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( updatedDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( updatedDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is( updatedDTO.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is( updatedDTO.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaIds", CoreMatchers.is( Collections.emptyList() )));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( DeliveryBoyCreateDTO.class ) );


        // Case: Delivery boy is not updated, status NOT FOUND
        BDDMockito.given( deliveryBoyService.update( Mockito.any( Integer.class ), Mockito.any( DeliveryBoyCreateDTO.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyCreateDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).update( Mockito.any( Integer.class ), Mockito.any( DeliveryBoyCreateDTO.class ) );

    }

    @Test
    void findById() throws Exception {
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( 1, "Test name", "Test surname", "Test phone number", Collections.emptyList());

        // Case: Delivery boy is found, status OK and DTO of found delivery boy is returned
        BDDMockito.given( deliveryBoyService.findById( Mockito.any( Integer.class ) ) ).willReturn( Optional.of(deliveryBoyDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryBoyDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryBoyDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is( deliveryBoyDTO.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is( deliveryBoyDTO.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaIds", CoreMatchers.is( Collections.emptyList() )));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );


        // Case: Delivery boy is not found, status NOT FOUND
        BDDMockito.given( deliveryBoyService.findById( Mockito.any( Integer.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .put("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).findById( Mockito.any( Integer.class ) );


    }

    @Test
    void findByDeliveryAreaName() throws Exception {
        // Create list of two delivery boys, with their delivery area and expect to get a list of json objects of these delivery boys based on the name of that area
        DeliveryArea deliveryArea = new DeliveryArea("Test name",Collections.emptyList() );
        DeliveryBoyDTO deliveryBoyDTO_A = new DeliveryBoyDTO( 1, "Test name A", "Test surname A", "Test phone number A", List.of(deliveryArea.getId()));
        DeliveryBoyDTO deliveryBoyDTO_B = new DeliveryBoyDTO( 2, "Test name B", "Test surname B", "Test phone number B", List.of(deliveryArea.getId()));


        // Case: Get delivery boys
        BDDMockito.given( deliveryBoyService.findAllByDeliveryAreaName( Mockito.anyString() )).willReturn( List.of( deliveryBoyDTO_A, deliveryBoyDTO_B) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_boy/area/{name}",deliveryArea.getName() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].id", Matchers.containsInAnyOrder( deliveryBoyDTO_A.id(), deliveryBoyDTO_B.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder( deliveryBoyDTO_A.name(), deliveryBoyDTO_B.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].surname", Matchers.containsInAnyOrder( deliveryBoyDTO_A.surname(), deliveryBoyDTO_B.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].phoneNumber", Matchers.containsInAnyOrder( deliveryBoyDTO_A.phoneNumber(), deliveryBoyDTO_B.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].deliveryAreaIds", CoreMatchers.hasItem( List.of(deliveryArea.getId()) ) ));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).findAllByDeliveryAreaName( deliveryArea.getName() );

    }

    @Test
    void findAll() throws Exception {
        // Create list of two delivery boys and expect to get a list of json objects of these delivery boys
        DeliveryBoyDTO deliveryBoyDTO_A = new DeliveryBoyDTO( 1, "Test name A", "Test surname A", "Test phone number A", Collections.emptyList());
        DeliveryBoyDTO deliveryBoyDTO_B = new DeliveryBoyDTO( 2, "Test name B", "Test surname B", "Test phone number B", Collections.emptyList());


        // Case: Get delivery boys
        BDDMockito.given( deliveryBoyService.findAll()).willReturn( List.of( deliveryBoyDTO_A, deliveryBoyDTO_B ) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/delivery_boy/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].id", Matchers.containsInAnyOrder( deliveryBoyDTO_A.id(), deliveryBoyDTO_B.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder( deliveryBoyDTO_A.name(), deliveryBoyDTO_B.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].surname", Matchers.containsInAnyOrder( deliveryBoyDTO_A.surname(), deliveryBoyDTO_B.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].phoneNumber", Matchers.containsInAnyOrder( deliveryBoyDTO_A.phoneNumber(), deliveryBoyDTO_B.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$[*].deliveryAreaIds", CoreMatchers.hasItem( Collections.emptyList() ) ));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).findAll();


    }

    @Test
    void deleteById() throws Exception {
        DeliveryBoyDTO deliveryBoyDTO = new DeliveryBoyDTO( 1, "Test name", "Test surname", "Test phone number", Collections.emptyList());

        // Case: Delivery boy is deleted, status OK and DTO of deleted delivery boy is returned
        BDDMockito.given( deliveryBoyService.deleteById( Mockito.any( Integer.class ) ) ).willReturn( Optional.of(deliveryBoyDTO) );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is( deliveryBoyDTO.id() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is( deliveryBoyDTO.name() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.surname", CoreMatchers.is( deliveryBoyDTO.surname() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.phoneNumber", CoreMatchers.is( deliveryBoyDTO.phoneNumber() )))
                .andExpect( MockMvcResultMatchers.jsonPath("$.deliveryAreaIds", CoreMatchers.is( Collections.emptyList() )));
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );


        // Case: Delivery boy is not found, status NOT FOUND
        BDDMockito.given( deliveryBoyService.deleteById( Mockito.any( Integer.class ) ) ).willReturn( Optional.empty() );
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/delivery_boy/{id}", deliveryBoyDTO.id() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept( MediaType.APPLICATION_JSON)
                        .content( toJsonString( deliveryBoyDTO ) )
        ).andExpect( MockMvcResultMatchers.status().isNotFound() );
        Mockito.verify( deliveryBoyService, Mockito.atLeastOnce()).deleteById( Mockito.any( Integer.class ) );

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

