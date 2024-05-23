package com.microservice1.microservice1.controller;

import com.microservice1.microservice1.dto.OperationDTO;
import com.microservice1.microservice1.mapper.OperacionMapper;
import com.microservice1.microservice1.model.Operation;
import com.microservice1.microservice1.model.User;
import com.microservice1.microservice1.repository.OperationRepository;
import com.microservice1.microservice1.repository.UserRepository;
import com.microservice1.microservice1.service.ValidationService;
import com.microservice1.microservice1.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api")
public class OperationController {

    private final UserRepository userRepository;
    private final OperationRepository operationRepository;
    private final OperacionMapper operacionMapper;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public OperationController(UserRepository userRepository,
                               OperationRepository operationRepository,
                               OperacionMapper operacionMapper,
                               ValidationService validationService,
                               KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
        this.operacionMapper = operacionMapper;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/operacion")
    public Mono<ResponseEntity<Operation>> createOperacion(@RequestBody OperationDTO operationDTO) {
        log.info("Received request to create operation: {}", operationDTO);

        Mono<User> userMono = (operationDTO.getUser().getId() != null)
                ? userRepository.findById(operationDTO.getUser().getId())
                : Mono.just(new User());

        return userMono
                .flatMap(existingUser -> {
                    if (existingUser.getId() == null) {
                        existingUser.setName(operationDTO.getUser().getName());
                        existingUser.setCreateAt(LocalDateTime.now());
                        existingUser.setUpdateAt(LocalDateTime.now());
                        return userRepository.save(existingUser);
                    } else {
                        return Mono.just(existingUser);
                    }
                })
                .flatMap(user -> {
                    Operation operacion = operacionMapper.toEntity(operationDTO);
                    operacion.setUserId(user.getId());
                    operacion.setCreatedAt(LocalDateTime.now());
                    operacion.setUpdatedAt(LocalDateTime.now());
                    return operationRepository.save(operacion)
                            .flatMap(savedOperacion -> kafkaProducer.sendMessage(savedOperacion.toString())
                                    .thenReturn(ResponseEntity.ok(savedOperacion)));
                });
    }

    @GetMapping("/send")
    public String sendMessage(@RequestParam("message") String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty";
        }
        kafkaProducer.sendMessage(message);
        return "Message sent to Kafka: " + message;
    }
}
