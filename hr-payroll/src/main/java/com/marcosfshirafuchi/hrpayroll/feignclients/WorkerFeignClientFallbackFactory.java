package com.marcosfshirafuchi.hrpayroll.feignclients;

import com.marcosfshirafuchi.hrpayroll.entities.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkerFeignClientFallbackFactory implements FallbackFactory<WorkerFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerFeignClientFallbackFactory.class);

    @Override
    public WorkerFeignClient create(Throwable cause) {
        // O log registrará a causa exata da falha no console
        LOGGER.error("Fallback acionado. Causa: ", cause);

        return new WorkerFeignClient() {
            @Override
            public ResponseEntity<Worker> findById(Long id) {
                // Retorna um trabalhador padrão como fallback
                Worker worker = new Worker();
                worker.setName("Brann");
                worker.setDailyIncome(400.0);
                return ResponseEntity.ok(worker);
            }
        };
    }
}
