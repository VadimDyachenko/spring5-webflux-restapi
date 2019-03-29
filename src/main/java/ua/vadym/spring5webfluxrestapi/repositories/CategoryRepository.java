package ua.vadym.spring5webfluxrestapi.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ua.vadym.spring5webfluxrestapi.domain.Category;

public interface CategoryRepository extends ReactiveMongoRepository<Category, String> {
}
