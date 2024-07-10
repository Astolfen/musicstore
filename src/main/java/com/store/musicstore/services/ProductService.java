package com.store.musicstore.services;

import com.store.musicstore.models.Image;
import com.store.musicstore.models.Product;
import com.store.musicstore.models.User;
import com.store.musicstore.models.enums.ProductType;
import com.store.musicstore.repositories.ProductRepository;
import com.store.musicstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public List<Product> listProduct(String title) {
        if (title != null && !title.isEmpty()) return productRepository.findByTitleContainingIgnoreCase(title);
        return productRepository.findAll();
    }

    public List<Product> listProductByTitleAndType(String title, ProductType productType) {
        if (title != null && !title.isEmpty()) {
            return productRepository.findByTitleContainingIgnoreCaseAndProductType(title, productType);
        } else {
            return productRepository.findByProductType(productType);
        }
    }


    public void saveProduct(Principal principal, Product product, MultipartFile file1,
                            MultipartFile file2, MultipartFile file3) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        productRepository.save(product);
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1, product.getId());
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }

        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2, product.getId());
            product.addImageToProduct(image2);
        }

        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3, product.getId());
            product.addImageToProduct(image3);
        }

        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        productRepository.save(product);
        product.setPreviewImageId(product.getImages().get(0).getId());
        productRepository.save(product);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    private Image toImageEntity(MultipartFile file, Long id) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String newOriginalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.')) + id
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path path = Paths.get(uploadPath, newOriginalFileName);
        Files.write(path, file.getBytes());

        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(newOriginalFileName);
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setFilePath(path.toString());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
