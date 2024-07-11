package ControllerTest;

import com.store.musicstore.controllers.ImageController;
import com.store.musicstore.models.Image;
import com.store.musicstore.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ImageControllerTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageController imageController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetImageById_ImageFound() throws MalformedURLException {
        Long id = 1L;
        Image mockImage = new Image();
        mockImage.setId(id);
        mockImage.setFilePath("/mock/path/to/image.jpg");
        mockImage.setOriginalFileName("image.jpg");
        mockImage.setContentType("image/jpeg");
        mockImage.setSize(1024L);

        when(imageRepository.getImageById(id)).thenReturn(mockImage);

        ResponseEntity<?> responseEntity = imageController.getImageById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("image/jpeg", responseEntity.getHeaders().getContentType().toString());
        assertEquals("image.jpg", responseEntity.getHeaders().getFirst("fileName"));
        assertEquals(1024L, responseEntity.getHeaders().getContentLength());
        verify(imageRepository, times(1)).getImageById(id);
    }

    @Test
    public void testGetImageById_ImageNotFound() throws MalformedURLException {
        Long id = 2L;
        when(imageRepository.getImageById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = imageController.getImageById(id);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(imageRepository, times(1)).getImageById(id);
    }
}

