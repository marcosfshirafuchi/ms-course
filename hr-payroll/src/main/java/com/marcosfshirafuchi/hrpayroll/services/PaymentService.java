package com.marcosfshirafuchi.hrpayroll.services;

import com.marcosfshirafuchi.hrpayroll.entities.Payment;
import com.marcosfshirafuchi.hrpayroll.entities.Worker;
import com.marcosfshirafuchi.hrpayroll.feignclients.WorkerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient workerFeignClient;

    public Payment getPayment(long workedId, int days){
        Worker worker = workerFeignClient.findById(workedId).getBody();
        return new Payment(worker.getName(),worker.getDailyIncome(),days);
    }
}
