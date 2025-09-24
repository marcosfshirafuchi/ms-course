package com.marcosfshirafuchi.hrpayroll.feignclients;

import com.marcosfshirafuchi.hrpayroll.entities.Worker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "hr-worker", path = "/workers", fallbackFactory = WorkerFeignClientFallbackFactory.class)
public interface WorkerFeignClient {
    @GetMapping(value = "/{id}")
    ResponseEntity<Worker> findById(@PathVariable Long id);
}