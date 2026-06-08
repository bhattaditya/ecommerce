package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.OrderItemRequest;
import com.example.ecommerce.dto.OrderRequestDTO;
import com.example.ecommerce.dto.OrderResponseDTO;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.PaymentFailedException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.exception.QuantityException;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.example.ecommerce.Constants.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;
    private final AuditService auditService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    private BigDecimal calculateTotalOrder(Order order) {
        return order.getItems().stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Order order = new Order();

        order.setUser(user);
        order.setOrderStatus(OrderStatus.CREATED);


        List<OrderItem> orderItems = new ArrayList<>();

        Map<Long, Integer> reservedStock = new HashMap<>();

        for (OrderItemRequest orderItemRequest: orderRequestDTO.items()) {

            Product product = productRepository.findById(orderItemRequest.productId()).
                    orElseThrow(() -> new ProductNotFoundException(orderItemRequest.productId()));

            int alreadyReservedStock = reservedStock.getOrDefault(product.getId(), 0);
            int availableStock = product.getStockQuantity() - alreadyReservedStock;

            OrderItem orderItem = getOrderItem(availableStock, orderItemRequest, product, order);
            reservedStock.merge(product.getId(), orderItemRequest.quantity(), Integer::sum);

            orderItems.add(orderItem);
        }

        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            int inventory = product.getStockQuantity();
            int requestedQuantity = item.getQuantity();
            product.setStockQuantity(inventory-requestedQuantity);
        }

        order.setItems(orderItems);
        order.setTotalAmount(calculateTotalOrder(order));
        orderRepository.save(order);
        auditService.logAction(ORDER_INITIATED, email, "Order ID: " + order.getId() + ", Total: " + order.getTotalAmount());
        String orderID = "Order" + UUID.randomUUID();

        Payment payment = new Payment();

        String payMethod = orderRequestDTO.paymentMethod();
        int attemptCount = paymentRepository.countByOrderId(order.getId()) + 1;
        boolean paid = paymentService.processPayment(orderID, payMethod, order.getTotalAmount());

        if (!paid) {
            payment.setOrder(order);
            payment.setAmount(order.getTotalAmount());
            payment.setAttemptNumber(attemptCount);
            payment.setStatus(PaymentStatus.FAILED);
            auditService.logAction(ORDER_FAILED, email, "Order ID: " + order.getId() + ", Total: " + order.getTotalAmount());
            paymentRepository.save(payment);
            throw new PaymentFailedException("Payment failed! Attempt: " + attemptCount);
        }


        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setAttemptNumber(attemptCount);
        paymentRepository.save(payment);

        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        auditService.logAction(ORDER_CONFIRMED, email, "Order ID: " + order.getId() + ", Total: " + order.getTotalAmount());

        return new OrderResponseDTO(orderID, order.getOrderStatus().name(), order.getTotalAmount());
    }

    private static OrderItem getOrderItem(int availableStock, OrderItemRequest orderItemRequest, Product product, Order order) {
        int requestedQuantity = orderItemRequest.quantity();

        if (availableStock < orderItemRequest.quantity()) {
            throw new QuantityException(
                    String.format("Insufficient stock for product '%s'. Requested: %d, Available: %d",
                            product.getName(), requestedQuantity, availableStock)
            );
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(requestedQuantity);
        orderItem.setPriceAtPurchase(product.getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }
}