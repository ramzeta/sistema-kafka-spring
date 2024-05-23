package com.microservice1.microservice1.dto;

import lombok.Data;

@Data
public class OperationDTO {
    private Long id;
    private UserDTO user;
    private String description;
    private String status;
}
