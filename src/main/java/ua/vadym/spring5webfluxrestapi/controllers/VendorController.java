package ua.vadym.spring5webfluxrestapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

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
    Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }
}