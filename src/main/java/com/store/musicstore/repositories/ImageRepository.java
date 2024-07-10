package com.store.musicstore.repositories;

import com.store.musicstore.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image getImageById(Long id);
}
