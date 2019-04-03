package ua.vadym.spring5webfluxrestapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class VendorControllerTest {

    private static final String BASE_URL = "/api/v1/vendors/";
    private static final String ID = "Vendor id";
    private WebTestClient webTestClient;
    private VendorRepository repository;

    @Before
    public void setUp() {
        repository = mock(VendorRepository.class);
        VendorController controller = new VendorController(repository);
        webTestClient = WebTestClient.bindToController(controller).build();
    }


    @Test
    public void getAllVendors() {
        Vendor vendorA = Vendor.builder().firstName("Jim").lastName("Bim").build();
        Vendor vendorB = Vendor.builder().firstName("Jack").lastName("Daniels").build();

        given(repository.findAll()).willReturn(Flux.just(vendorA, vendorB));

        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        Vendor vendor = Vendor.builder().firstName("Jim").lastName("Bim").build();
        given(repository.findById(ID)).willReturn(Mono.just(vendor));

        webTestClient.get()
                .uri(BASE_URL + ID)
                .exchange()
                .expectBody(Vendor.class)
                .isEqualTo(vendor);
    }


    @Test
    public void createVendor() {
        given(repository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Vendor").build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void updateVendor() {
        Mono<Vendor> savedVendor = Mono.just(Vendor.builder().id(ID).firstName("Vendor").build());
        given(repository.save(any(Vendor.class))).willReturn(savedVendor);

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Vendor").build());

        webTestClient.put()
                .uri(BASE_URL + ID)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class).isEqualTo(savedVendor.block());
    }

    @Test
    public void patchVendor() {
        Mono<Vendor> savedVendor = Mono.just(Vendor.builder().id(ID).firstName("Vendor").build());
        given(repository.save(any(Vendor.class))).willReturn(savedVendor);

        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Vendor").build());

        webTestClient.patch()
                .uri(BASE_URL + ID)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class).isEqualTo(savedVendor.block());
    }
}
