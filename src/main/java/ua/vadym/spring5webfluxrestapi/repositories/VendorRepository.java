package ua.vadym.spring5webfluxrestapi.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
