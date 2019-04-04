package ua.vadym.spring5webfluxrestapi.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;

public interface VendorService {
    Flux<Vendor> getAllVendors();
    Mono<Vendor> findVendorById(String id);
    Mono<Vendor> createVendor(Vendor vendor);
    Mono<Vendor> updateVendor(String id, Vendor vendor);
    Mono<Vendor> patchVendor(String id, Vendor vendor);

 }
