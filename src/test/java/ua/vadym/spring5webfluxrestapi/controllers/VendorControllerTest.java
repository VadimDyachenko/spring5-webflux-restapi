package ua.vadym.spring5webfluxrestapi.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendorControllerTest {

    private static final String BASE_URL = "/api/v1/vendors/";
    private static final String ID_1 = "Vendor id 1";
    private static final String ID_2 = "Vendor id 2";

    private Vendor vendorA;
    private Vendor vendorB;

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private VendorRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll().block();

        vendorA = Vendor.builder().id(ID_1).firstName("Jim").lastName("Bim").build();
        vendorB = Vendor.builder().id(ID_2).firstName("Jack").lastName("Daniels").build();

        repository.save(vendorA).block();
        repository.save(vendorB).block();
    }

    @Test
    public void getAllVendors() {
        webTestClient.get()
                .uri(BASE_URL)
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2)
                .contains(vendorA, vendorB);
    }

    @Test
    public void getVendorById() {
        webTestClient.get()
                .uri(BASE_URL + ID_1)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class)
                .isEqualTo(vendorA);
    }

    @Test
    public void getVendorByIdNotFound() {
        webTestClient.get()
                .uri(BASE_URL + "absent id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createVendor() {
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Vendor").lastName("Last Name").build());

        webTestClient.post()
                .uri(BASE_URL)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("Vendor");

        Long count = repository.count().block();
        assertNotNull(count);
        assertEquals(3L, count.longValue());
    }

    @Test
    public void updateVendor() {
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("New Vendor").lastName("New last name").build());

        Vendor expected = Vendor.builder().id(ID_1).firstName("New Vendor").lastName("New last name").build();

        webTestClient.put()
                .uri(BASE_URL + ID_1)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class).isEqualTo(expected);
    }

    @Test
    public void patchVendorFirstNameField() {
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("New First Name").build());

        Vendor expected = Vendor.builder().id(ID_1).firstName("New First Name").lastName("Bim").build();

        webTestClient.patch()
                .uri(BASE_URL + ID_1)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class).isEqualTo(expected);
    }

    @Test
    public void patchVendorLastNameField() {
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().lastName("New Last Name").build());

        Vendor expected = Vendor.builder().id(ID_1).firstName("Jim").lastName("New Last Name").build();

        webTestClient.patch()
                .uri(BASE_URL + ID_1)
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Vendor.class).isEqualTo(expected);
    }

    @Test
    public void patchVendorNotFound() {
        Mono<Vendor> vendorMono = Mono.just(Vendor.builder().firstName("Vendor").build());

        webTestClient.patch()
                .uri(BASE_URL + "absent id")
                .body(vendorMono, Vendor.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}

