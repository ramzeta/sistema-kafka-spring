package com.microservice1.microservice1.dto;

import lombok.Data;

import java.time.Instant;


@Data
public class UserDTO {
    private Long id;
    private String name;
    private Instant createAt;
    private Instant updateAt;
}
