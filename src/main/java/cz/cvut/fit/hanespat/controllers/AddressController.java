package cz.cvut.fit.hanespat.controllers;

import cz.cvut.fit.hanespat.data.dto.AddressDTO;
import cz.cvut.fit.hanespat.data.dto.create.AddressCreateDTO;
import cz.cvut.fit.hanespat.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressDTO create (@RequestBody AddressCreateDTO addressDTO ) {
        return addressService.create( addressDTO ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @PutMapping("/address/{id}")
    public AddressDTO update ( @PathVariable int id, @RequestBody AddressCreateDTO addressDTO ) {
        return addressService.update( id, addressDTO ).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/address/{id}")
    public AddressDTO findById ( @PathVariable int id ) {
        return addressService.findById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }

    @GetMapping("/address/all")
    public List<AddressDTO> findAll () {
        return addressService.findAll();
    }

    @DeleteMapping("/address/{id}")
    public AddressDTO deleteById ( @PathVariable int id ) {
        return addressService.deleteById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND ) );
    }
}

