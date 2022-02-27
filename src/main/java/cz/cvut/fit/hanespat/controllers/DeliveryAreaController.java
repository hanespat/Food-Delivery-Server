package cz.cvut.fit.hanespat.controllers;

import cz.cvut.fit.hanespat.data.dto.DeliveryAreaDTO;
import cz.cvut.fit.hanespat.data.dto.create.DeliveryAreaCreateDTO;
import cz.cvut.fit.hanespat.services.DeliveryAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class DeliveryAreaController {

    private final DeliveryAreaService deliveryAreaService;

    @Autowired
    public DeliveryAreaController( DeliveryAreaService deliveryAreaService ) {
        this.deliveryAreaService = deliveryAreaService;
    }

    @PostMapping("/delivery_area")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryAreaDTO create ( @RequestBody DeliveryAreaCreateDTO deliveryArea ) {
        return deliveryAreaService.create( deliveryArea ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/delivery_area/{id}")
    public DeliveryAreaDTO findById ( @PathVariable int id ) {
        return deliveryAreaService.findById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/delivery_area/all")
    public List<DeliveryAreaDTO> findAll () {
        return deliveryAreaService.findAll();
    }

    @PutMapping("/delivery_area/{id}")
    public DeliveryAreaDTO update ( @PathVariable int id, @RequestBody DeliveryAreaCreateDTO deliveryArea ) {
        return deliveryAreaService.update( id, deliveryArea ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @DeleteMapping("/delivery_area/{id}")
    public DeliveryAreaDTO deleteById ( @PathVariable int id ) {
        return deliveryAreaService.deleteById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

}

