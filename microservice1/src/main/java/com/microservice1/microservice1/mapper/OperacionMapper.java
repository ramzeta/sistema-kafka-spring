package com.microservice1.microservice1.mapper;

import com.microservice1.microservice1.dto.OperationDTO;
import com.microservice1.microservice1.dto.UserDTO;
import com.microservice1.microservice1.model.Operation;
import com.microservice1.microservice1.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface OperacionMapper {
    OperacionMapper INSTANCE = Mappers.getMapper(OperacionMapper.class);
    OperationDTO toDto(Operation operation);
    Operation toEntity(OperationDTO operationDTO);

    @Named("mapLocalDateTimeToInstant")
    default Instant map(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    @Named("mapInstantToLocalDateTime")
    default LocalDateTime map(Instant value) {
        return value == null ? null : LocalDateTime.ofInstant(value, ZoneOffset.UTC);
    }

    @Mapping(source = "createAt", target = "createAt", qualifiedByName = "mapLocalDateTimeToInstant")
    @Mapping(source = "updateAt", target = "updateAt", qualifiedByName = "mapLocalDateTimeToInstant")
    UserDTO toDto(User user);

    @Mapping(source = "createAt", target = "createAt", qualifiedByName = "mapInstantToLocalDateTime")
    @Mapping(source = "updateAt", target = "updateAt", qualifiedByName = "mapInstantToLocalDateTime")
    User toEntity(UserDTO userDTO);
}
