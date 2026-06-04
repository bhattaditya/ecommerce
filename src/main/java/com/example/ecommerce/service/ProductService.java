package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    Product getProductById(Long id);
    List<Product> sortByPriceAsc();
    List<Product> sortByPriceDesc();
    List<Product> getProductByRange(BigDecimal min, BigDecimal max);
    List<Product> getProductsByCategory(String category);
    Map<String, List<Product>> groupProductsByCategory();
    int calculateTotalStock(List<Product> products);
    int expensiveProduct(List<Product> products);
    Optional<Product>  expensiveProductPrice(List<Product> products);
}
