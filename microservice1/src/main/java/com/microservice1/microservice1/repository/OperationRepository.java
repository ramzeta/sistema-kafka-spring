
package com.microservice1.microservice1.repository;

import com.microservice1.microservice1.model.Operation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends ReactiveCrudRepository<Operation, Long> {
}