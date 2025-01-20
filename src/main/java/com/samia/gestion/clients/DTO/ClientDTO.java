package com.samia.gestion.clients.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.samia.gestion.clients.entity.Sex;

import java.time.LocalDate;
import java.util.List;

public record ClientDTO(
        Long id,
        String firstName,
        String lastName,
        String comments,
        String email,
        String mobilePhone,
        String homePhone,
        @JsonProperty("birthday")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthday,
        String city,
        String streetName,
        String zipCode,
        LocalDate created,
        LocalDate modified,
        Sex sex,
        Long userId,
        List<ProgramDTO> programDTOS
) {}
