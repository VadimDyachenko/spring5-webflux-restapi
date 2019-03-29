package ua.vadym.spring5webfluxrestapi.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.vadym.spring5webfluxrestapi.domain.Category;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.CategoryRepository;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        addVendors();
        addCategory();
    }

    private void addCategory() {
        Long count = categoryRepository.count().block();
        if(count != null && count == 0) {
            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();

            log.info("Laded categories data, {} items", categoryRepository.count().block());
        } else {
            log.info("Category collection already exists and contains data. Loading categories skipped.");
        }
    }

    private void addVendors() {
        Long count = vendorRepository.count().block();
        if(count != null && count == 0) {
            vendorRepository.save(Vendor.builder().firstName("Jerry").lastName("M").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Tom").lastName("C").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Super").lastName("L").build()).block();

            log.info("Laded vendors data, {} items", vendorRepository.count().block());
        } else {
            log.info("Vendor collection already exists and contains data. Loading vendors skipped." );
        }
    }
}
