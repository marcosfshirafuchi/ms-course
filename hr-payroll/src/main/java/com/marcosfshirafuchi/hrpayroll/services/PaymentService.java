package com.marcosfshirafuchi.hrpayroll.services;

import com.marcosfshirafuchi.hrpayroll.entities.Payment;
import com.marcosfshirafuchi.hrpayroll.entities.Worker;
import com.marcosfshirafuchi.hrpayroll.feignclients.WorkerFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import com.marcosfshirafuchi.hrpayroll.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaymentService {

    @Autowired
    private WorkerFeignClient workerFeignClient;

    public Payment getPayment(long workedId, int days){
        Worker worker = workerFeignClient.findById(workedId).getBody();

        if (Objects.isNull(worker)) {
            throw new ResourceNotFoundException("Trabalhador não encontrado para o ID: " + workedId);
        }

        return new Payment(worker.getName(), worker.getDailyIncome(), days);
    }
}
