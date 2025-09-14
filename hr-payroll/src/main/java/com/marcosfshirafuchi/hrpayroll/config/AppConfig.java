package com.marcosfshirafuchi.hrpayroll.config;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    ServiceInstanceListSupplier hrWorkerInstanceSupplier() {
        return new ServiceInstanceListSupplier() {
            @Override
            public String getServiceId() {
                return "hr-worker";
            }

            @Override
            public Flux<List<ServiceInstance>> get() {
                return Flux.just(List.of(
                        new DefaultServiceInstance("hr-worker-1", "hr-worker", "localhost", 8001, false),
                        new DefaultServiceInstance("hr-worker-2", "hr-worker", "localhost", 8002, false)
                ));
            }
        };
    }
}
