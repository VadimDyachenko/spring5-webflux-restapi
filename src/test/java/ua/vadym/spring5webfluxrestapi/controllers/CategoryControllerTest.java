package ua.vadym.spring5webfluxrestapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Category;
import ua.vadym.spring5webfluxrestapi.repositories.CategoryRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CategoryControllerTest {

    private static final String BASE_URL = "/api/v1/categories/";
    private WebTestClient webTestClient;
    private CategoryRepository repository;

    @Before
    public void setUp() {
        repository = mock(CategoryRepository.class);
        CategoryController controller = new CategoryController(repository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void getAllCategories() {
        Category fruits = Category.builder().description("Fruits").build();
        Category nuts = Category.builder().description("Nuts").build();

        given(repository.findAll()).willReturn(Flux.just(fruits, nuts));

        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getCategoryById() {
        String id = "category id";
        Category nuts = Category.builder().description("Nuts").build();
        given(repository.findById(id)).willReturn(Mono.just(nuts));

        webTestClient.get()
                .uri(BASE_URL + id)
                .exchange()
                .expectBody(Category.class)
                .isEqualTo(nuts);
    }

    @Test
    public void createCategory() {
        given(repository.saveAll(any(Publisher.class))).willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("Category").build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }
}