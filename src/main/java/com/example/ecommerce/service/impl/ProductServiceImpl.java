package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override

    public List<Product> sortByPriceAsc() {
        return repository.findAll(
                Sort.by(Sort.Direction.ASC, "price"));
    }

    @Override

    public List<Product> sortByPriceDesc() {
        return repository.findAll(
                Sort.by(Sort.Direction.DESC, "price"));
    }

    @Override

    public List<Product> getProductByRange(BigDecimal min, BigDecimal max) {
        return repository.findByPriceBetween(min, max);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategoryIgnoreCase(category);

    }

    @Override
    public Map<String, List<Product>> groupProductsByCategory() {
        return repository.findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Product::getCategory));
    }

    @Override
    public int calculateTotalStock(List<Product> products) {
        return products.stream()
                .map(Product::getStockQuantity)
                .reduce(0, Integer::sum);

    }

    @Override
    public int expensiveProduct(List<Product> products) {
        return Math.toIntExact(products.stream()
                .filter(p -> p.getPrice()
                        .compareTo(BigDecimal.valueOf(50000)) > 0)
                .count());
    }

    @Override
    public Optional<Product> expensiveProductPrice(List<Product> products) {
        return products.stream()
                .max(Comparator.comparing(Product::getPrice));
    }

}
