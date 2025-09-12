package com.marcosfshirafuchi.hrpayroll.services;

import com.marcosfshirafuchi.hrpayroll.entities.Payment;
import com.marcosfshirafuchi.hrpayroll.entities.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${hr-worker.host}")
    private String workerHost;


    @Autowired
    private RestTemplate restTemplate;

    public Payment getPayment(long workedId, int days){
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "" + workedId);
        Worker worker = restTemplate.getForObject(workerHost + "/workers/{id}", Worker.class, uriVariables);
        return new Payment(worker.getName(),worker.getDailyIncome(),days);
    }
}
