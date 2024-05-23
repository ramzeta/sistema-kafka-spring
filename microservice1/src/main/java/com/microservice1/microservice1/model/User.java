package com.microservice1.microservice1.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Table("user")
public class User {
    @Id
    private Long id;
    private String name;
    @Column("created_at")
    private LocalDateTime createAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
}
