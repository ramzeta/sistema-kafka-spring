package com.microservice1.microservice1.service;

import org.springframework.stereotype.Service;
import com.microservice1.microservice1.dto.OperationDTO;

@Service
public class ValidationService {
    public boolean validate(OperationDTO operationDTO) {
        return operationDTO.getUser() != null && operationDTO.getUser().getName() != null && !operationDTO.getUser().getName().isEmpty();
    }

}