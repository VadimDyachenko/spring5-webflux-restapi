package ua.vadym.spring5webfluxrestapi.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import java.util.function.Function;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Flux<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public Mono<Vendor> findVendorById(String id) {
        return vendorRepository.findById(id);
    }

    @Override
    public Mono<Vendor> createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @Override
    public Mono<Vendor> updateVendor(String id, Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @Override
    public Mono<Vendor> patchVendor(String id, Vendor vendor) {
        return vendorRepository.findById(id)
                .flatMap(patchVendorFunction(vendor));
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
