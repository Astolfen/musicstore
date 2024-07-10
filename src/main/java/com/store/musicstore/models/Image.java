package com.store.musicstore.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String originalFileName;

    private Long size;

    private String contentType;

    private boolean isPreviewImage;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    private Product product;

    @PreRemove
    private void removeImageStore() throws IOException {
        Path path = Paths.get(this.filePath);
        Files.deleteIfExists(path);
    }
}
