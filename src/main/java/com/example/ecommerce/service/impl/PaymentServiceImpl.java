package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.PaymentMethod;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public boolean processPayment(String orderID, String paymentMethod, double amount){
        PaymentMethod paymentMethodEnum = PaymentMethod.valueOf(paymentMethod);
        //save payment details in DB
        return true;
    }

}
