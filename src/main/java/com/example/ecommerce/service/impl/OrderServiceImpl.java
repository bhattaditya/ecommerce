package com.example.ecommerce.service.impl;


import com.example.ecommerce.dto.OrderRequestDTO;
import com.example.ecommerce.dto.OrderResponseDTO;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.PaymentFailedException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.exception.QuantityException;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentServiceImpl paymentService;
    private final UserRepository userRepository;;

    @Override
    public BigDecimal calculateTotalOrder(Order order) {
        return order.getItems().stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        long productId = orderRequestDTO.productId();

        Product product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException(productId));

        int inventory = product.getStockQuantity();
        int requestedQuantity = orderRequestDTO.requestedQuantity();

        if (inventory <= 0 || inventory < requestedQuantity) {
            throw new QuantityException("Out of Stock!");
        }

        String payMethod = orderRequestDTO.paymentMethod();
        boolean isPaymentSuccess = paymentService.processPayment(UUID.randomUUID().toString(), payMethod, product.getPrice().doubleValue());

        if (!isPaymentSuccess) {
            throw new PaymentFailedException("Payment failed!");
        }

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Order order = new Order();
        OrderItem orderItem = new OrderItem();

        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setPriceAtPurchase(product.getPrice());

        order.setItems(List.of(orderItem));
        orderRepository.save(order);

        product.setStockQuantity(inventory-requestedQuantity);
        productRepository.save(product);
        String orderID = "Order" + UUID.randomUUID();
        return new OrderResponseDTO(orderID, null, 0);
    }
}