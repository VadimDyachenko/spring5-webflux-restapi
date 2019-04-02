package ua.vadym.spring5webfluxrestapi.bootstrap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.vadym.spring5webfluxrestapi.domain.Category;
import ua.vadym.spring5webfluxrestapi.domain.Vendor;
import ua.vadym.spring5webfluxrestapi.repositories.CategoryRepository;
import ua.vadym.spring5webfluxrestapi.repositories.VendorRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootstrapTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private Bootstrap bootstrap;

    @Test
    public void shouldLoadDataOnStartup() {
        //given
        categoryRepository.deleteAll().block();
        vendorRepository.deleteAll().block();

        //when
        bootstrap.run();

        //then
        List<Category> categories = categoryRepository.findAll().collectList().block();
        List<Vendor> vendors = vendorRepository.findAll().collectList().block();

        assertNotNull(categories);
        assertEquals(5, categories.size());
        assertNotNull(vendors);
        assertEquals(3, vendors.size());
    }
}