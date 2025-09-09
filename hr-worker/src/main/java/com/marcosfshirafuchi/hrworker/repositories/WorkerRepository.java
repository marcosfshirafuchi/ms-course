package com.marcosfshirafuchi.hrworker.repositories;

import com.marcosfshirafuchi.hrworker.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
}
