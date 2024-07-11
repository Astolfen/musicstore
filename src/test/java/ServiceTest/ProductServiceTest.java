package ServiceTest;

import com.store.musicstore.models.Image;
import com.store.musicstore.models.Product;
import com.store.musicstore.models.User;
import com.store.musicstore.models.enums.ProductType;
import com.store.musicstore.repositories.ProductRepository;
import com.store.musicstore.repositories.UserRepository;
import com.store.musicstore.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    private String uploadPath = "uploads";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository, userRepository);
    }

    @Test
    public void testListProductWithTitle() {
        String title = "Test";
        Product mockProduct = new Product();
        mockProduct.setTitle(title);
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(mockProduct);

        when(productRepository.findByTitleContainingIgnoreCase(title)).thenReturn(mockProducts);

        List<Product> products = productService.listProduct(title);

        assertEquals(1, products.size());
        assertEquals(title, products.get(0).getTitle());
        verify(productRepository).findByTitleContainingIgnoreCase(title);
    }

    @Test
    public void testListProductByTitleAndType() {
        String title = "Test";
        ProductType productType = ProductType.GUITAR;
        Product mockProduct = new Product();
        mockProduct.setTitle(title);
        mockProduct.setProductType(productType);
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(mockProduct);

        when(productRepository.findByTitleContainingIgnoreCaseAndProductType(title, productType)).thenReturn(mockProducts);

        List<Product> products = productService.listProductByTitleAndType(title, productType);

        assertEquals(1, products.size());
        assertEquals(title, products.get(0).getTitle());
        assertEquals(productType, products.get(0).getProductType());
        verify(productRepository).findByTitleContainingIgnoreCaseAndProductType(title, productType);
    }

    @Test
    public void testGetUserByPrincipal() {
        String userEmail = "testuser@example.com";
        Principal principal = () -> userEmail;
        User mockUser = new User();
        mockUser.setEmail(userEmail);

        when(userRepository.findByEmail(principal.getName())).thenReturn(mockUser);

        User user = productService.getUserByPrincipal(principal);

        assertNotNull(user);
        assertEquals(userEmail, user.getEmail());
        verify(userRepository).findByEmail(principal.getName());
    }

    @Test
    public void testGetProductById() {
        Long productId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        Product product = productService.getProductById(productId);

        assertNotNull(product);
        assertEquals(productId, product.getId());
        verify(productRepository).findById(productId);
    }
}
