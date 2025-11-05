package com.marcosfshirafuchi.hrworker.resources;

import com.marcosfshirafuchi.hrworker.entities.Worker;
import com.marcosfshirafuchi.hrworker.repositories.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RefreshScope
@RestController
@RequestMapping(value = "/workers")
public class WorkerResource {

    private Logger logger = LoggerFactory.getLogger(WorkerResource.class);

    @Value("${test.config:valor-padrao}")
    private String testConfig;


    @Autowired
    private Environment env;

    //Injeção de dependência
    @Autowired
    private WorkerRepository repository;

    @GetMapping(value = "/configs")
    public ResponseEntity<Void> getConfigs(){
        logger.info("CONFIG = " + testConfig);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Worker>> findAll(){
        List<Worker> list = repository.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Worker> findById(@PathVariable Long id){
        logger.info("PORT = " + env.getProperty("local.server.port"));
        try{
        // Simulando delay de 3 segundos
        Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Worker obj = repository.findById(id).get();
        return ResponseEntity.ok(obj);
    }
}
