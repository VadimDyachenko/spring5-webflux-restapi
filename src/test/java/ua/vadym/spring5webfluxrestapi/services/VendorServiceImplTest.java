package ua.vadym.spring5webfluxrestapi.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class VendorServiceImplTest {

    private static final String BASE_URL = "/api/v1/vendors/";
    private static final String ID = "Vendor id";

    @Mock
    private VendorRepository repository;

    private VendorService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new VendorServiceImpl(repository);
    }


    @Test
    public void getAllVendors() {
        Vendor vendorA = Vendor.builder().firstName("Jim").lastName("Bim").build();
        Vendor vendorB = Vendor.builder().firstName("Jack").lastName("Daniels").build();

        given(repository.findAll()).willReturn(Flux.just(vendorA, vendorB));

        List<Vendor> vendors = service.getAllVendors().collectList().block();

        assertNotNull(vendors);
        assertEquals(2, vendors.size());
        assertTrue(vendors.contains(vendorA));
        assertTrue(vendors.contains(vendorB));

        verify(repository).findAll();
    }

    @Test
    public void findVendorById() {
        Vendor vendor = Vendor.builder().id(ID).firstName("Jim").lastName("Bim").build();
        given(repository.findById(ID)).willReturn(Mono.just(vendor));

        Vendor actual = service.findVendorById(ID).block();

        assertThat(actual, is(vendor));
    }

    @Test
    public void createVendor() {
        given(repository.save(any(Vendor.class))).willReturn(Mono.just(Vendor.builder().id(ID).build()));

        Vendor vendorToSave = Vendor.builder().firstName("Vendor").build();

        service.createVendor(vendorToSave).block();

        verify(repository).save(vendorToSave);

    }

}