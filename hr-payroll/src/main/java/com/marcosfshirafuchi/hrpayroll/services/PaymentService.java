package com.marcosfshirafuchi.hrpayroll.services;

import com.marcosfshirafuchi.hrpayroll.entities.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public Payment getPayment(long workedId, int days){
 
        return new Payment("Bob",200.0,days);
    }
}
