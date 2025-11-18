package edu.guvi.kaddysShop.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import edu.guvi.kaddysShop.Model.Product;
import edu.guvi.kaddysShop.Repository.ProductRepository;
import edu.guvi.kaddysShop.Service.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository repo;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

       
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void testFindAll() {
        Product p1 = new Product();
        Product p2 = new Product();
        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.findAll();

        assertThat(result).hasSize(2);
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Product p = new Product();
        p.setName("Test Product");

        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product result = productService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Product");
    }

    @Test
    void testSaveProductWithImage() throws Exception {
        Product p = new Product();
        p.setName("Test Product");

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenReturn("dummy".getBytes());

        Map<String, Object> mockUploadResult = new HashMap<>();
        mockUploadResult.put("secure_url", "http://cloudinary.com/image.jpg");

        when(uploader.upload(any(byte[].class), anyMap()))
                .thenReturn(mockUploadResult);

        when(repo.save(p)).thenReturn(p);

        Product result = productService.save(p, multipartFile);

        assertThat(result.getImage()).isEqualTo("http://cloudinary.com/image.jpg");
        verify(repo).save(p);
    }

    @Test
    void testSaveProductWithoutImage() {
        Product p = new Product();
        p.setName("No Image Product");

        when(multipartFile.isEmpty()).thenReturn(true);

        when(repo.save(p)).thenReturn(p);

        Product result = productService.save(p, multipartFile);

        assertThat(result.getImage()).isNull();  
        verify(repo).save(p);
    }

    @Test
    void testDeleteProduct() {
        productService.delete(10L);
        verify(repo, times(1)).deleteById(10L);
    }
}
