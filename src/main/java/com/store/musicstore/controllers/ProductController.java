package com.store.musicstore.controllers;

import com.store.musicstore.models.Product;
import com.store.musicstore.models.enums.ProductType;
import com.store.musicstore.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "productType", required = false) ProductType productType,
                           Principal principal, Model model) {

        List<Product> products;

        if (productType != null) {
            products = productService.listProductByTitleAndType(title, productType);
        } else {
            products = productService.listProduct(title);
        }
        model.addAttribute("products", products);
        model.addAttribute("user",productService.getUserByPrincipal(principal));
        model.addAttribute("productTypes",  Arrays.asList(ProductType.values()));
        return "products";
    }


    @GetMapping("product/{id}")
    public String productInfo(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("ims", product.getImages());
        return "product-info";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product,
                                Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/";
    }

    @PostMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
