package com.example.sanitasapp.services.ServicesImpl;

import com.example.sanitasapp.models.Payment;
import com.example.sanitasapp.repository.PaymentRepository;
import com.example.sanitasapp.services.PaymentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentSerImpl implements PaymentServices {
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentSerImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void addPaymentToInventory(Payment payment){
        paymentRepository.save(payment);
    }
}
