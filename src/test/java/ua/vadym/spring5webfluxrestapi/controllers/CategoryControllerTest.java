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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CategoryControllerTest {

    private static final String BASE_URL = "/api/v1/categories/";
    private static final String ID = "category id";
    private static final String DESCRIPTION = "Category";
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
        Category nuts = Category.builder().description("Nuts").build();
        given(repository.findById(ID)).willReturn(Mono.just(nuts));

        webTestClient.get()
                .uri(BASE_URL + ID)
                .exchange()
                .expectBody(Category.class)
                .isEqualTo(nuts);
    }

    @Test
    public void createCategory() {
        given(repository.saveAll(any(Publisher.class))).willReturn(Flux.just(Category.builder().build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description(DESCRIPTION).build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void updateCategory() {
        Mono<Category> savedCategory = Mono.just(Category.builder().id(ID).description(DESCRIPTION).build());
        given(repository.save(any(Category.class))).willReturn(savedCategory);

        Mono<Category> categoryMono = Mono.just(Category.builder().description(DESCRIPTION).build());

        webTestClient.put()
                .uri(BASE_URL + ID)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class).isEqualTo(savedCategory.block());
    }

    @Test
    public void patchCategoryWithChanges() {
        Mono<Category> originalCategory = Mono.just(Category.builder().id(ID).description("Old category").build());
        given(repository.findById(ID)).willReturn(originalCategory);

        Mono<Category> savedCategory = Mono.just(Category.builder().id(ID).description(DESCRIPTION).build());
        given(repository.save(any(Category.class))).willReturn(savedCategory);

        Mono<Category> categoryMono = Mono.just(Category.builder().description(DESCRIPTION).build());

        webTestClient.patch()
                .uri(BASE_URL + ID)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class).isEqualTo(savedCategory.block());

        verify(repository).findById(ID);
        verify(repository).save(savedCategory.block());
    }

    @Test
    public void patchCategoryWithoutChanges() {
        Mono<Category> originalCategory = Mono.just(Category.builder().id(ID).description(DESCRIPTION).build());
        given(repository.findById(ID)).willReturn(originalCategory);

        Mono<Category> categoryMono = Mono.just(Category.builder().description(DESCRIPTION).build());

        webTestClient.patch()
                .uri(BASE_URL + ID)
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Category.class).isEqualTo(originalCategory.block());

        verify(repository).findById(ID);
        verify(repository, never()).save(any(Category.class));
    }

}