package cz.cvut.fit.hanespat.controllers;

import cz.cvut.fit.hanespat.data.dto.DeliveryBoyDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryBoyCreateDTO;
import cz.cvut.fit.hanespat.services.DeliveryBoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class DeliveryBoyController {

    private final DeliveryBoyService deliveryBoyService;

    @Autowired
    public DeliveryBoyController(DeliveryBoyService deliveryBoyService) {
        this.deliveryBoyService = deliveryBoyService;
    }

    @PostMapping("/delivery_boy")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryBoyDTO create ( @RequestBody DeliveryBoyCreateDTO deliveryBoy ) {
        return deliveryBoyService.create( deliveryBoy ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @PutMapping("/delivery_boy/{id}")
    public DeliveryBoyDTO update ( @PathVariable int id, @RequestBody DeliveryBoyCreateDTO deliveryBoy ) {
        return deliveryBoyService.update( id, deliveryBoy ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/delivery_boy/{id}")
    public DeliveryBoyDTO findById ( @PathVariable int id ) {
        return deliveryBoyService.findById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/delivery_boy/area/{name}")
    public List<DeliveryBoyDTO> findByDeliveryAreaName ( @PathVariable String name ) {
        return deliveryBoyService.findAllByDeliveryAreaName( name );
    }

    @GetMapping("/delivery_boy/all")
    public List<DeliveryBoyDTO> findAll () {
        return deliveryBoyService.findAll();
    }

    @DeleteMapping("/delivery_boy/{id}")
    public DeliveryBoyDTO deleteById ( @PathVariable int id ) {
        return deliveryBoyService.deleteById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }
}

