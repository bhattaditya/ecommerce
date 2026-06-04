package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/group-by-category")
    public Map<String, List<Product>> groupProductsByCategory() {
        return service.groupProductsByCategory();
    }

    @GetMapping("/get-by-category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return service.getProductsByCategory(category);
    }

    @GetMapping("get-product/{id}")
    public Product getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @GetMapping("get-product-between-range/{min}/{max}")
    public List<Product> getProductByRange(@PathVariable BigDecimal min, @PathVariable BigDecimal max) {
        return service.getProductByRange(min, max);
    }

    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) String sort) {

        if ("desc".equalsIgnoreCase(sort)) {
            return service.sortByPriceDesc();
        }

        return service.sortByPriceAsc();
    }

}
