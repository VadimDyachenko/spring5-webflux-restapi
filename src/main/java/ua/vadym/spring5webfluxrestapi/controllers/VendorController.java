package ua.vadym.spring5webfluxrestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    Flux<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<Vendor>> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    Mono<ResponseEntity<Vendor>> createVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor)
                .map(v -> new ResponseEntity<>(v, HttpStatus.CREATED))
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<Vendor>> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PatchMapping("/{id}")
    Mono<ResponseEntity<Vendor>> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id)
                .flatMap(patchVendorFunction(vendor))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Function<Vendor, Mono<? extends Vendor>> patchVendorFunction(Vendor vendor) {
        return existingVendor -> {
            String firstName = vendor.getFirstName();
            if (firstName != null) {
                existingVendor.setFirstName(firstName);
            }

            String lastName = vendor.getLastName();
            if (lastName != null) {
                existingVendor.setLastName(lastName);
            }
            return vendorRepository.save(existingVendor);
        };
    }
}
