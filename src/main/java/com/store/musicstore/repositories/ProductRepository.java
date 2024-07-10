package com.store.musicstore.repositories;

import com.store.musicstore.models.Product;
import com.store.musicstore.models.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByProductType(ProductType productType);

    List<Product> findByTitleContainingIgnoreCaseAndProductType(String title, ProductType productType);
}
