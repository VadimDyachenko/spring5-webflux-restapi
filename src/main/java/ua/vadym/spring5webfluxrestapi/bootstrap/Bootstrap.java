package ua.vadym.spring5webfluxrestapi.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;

    @Autowired
    public Bootstrap(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) {
        addVendors();
    }

    private void addVendors() {

        if(vendorRepository.count().block() == 0) {
            vendorRepository.save(Vendor.builder().firstName("Jerry").lastName("M").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Tom").lastName("C").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Super").lastName("L").build()).block();

            log.info("Laded vendors data, {} items", vendorRepository.count().block());
        }
    }
}
