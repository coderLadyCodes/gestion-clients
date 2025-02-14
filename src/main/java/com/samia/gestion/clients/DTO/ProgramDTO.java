package com.samia.gestion.clients.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record ProgramDTO(@JsonProperty("id") Long id, Long userId, Long clientId, String programReference,LocalDate createdDate,
                         BigDecimal totalProgramPrice,
                         @JsonProperty("cares")List<CareDTO> careDTOList) {
    public ProgramDTO {
        if (careDTOList == null) {
            careDTOList = Collections.emptyList();
        }
    }
//    public ProgramDTO(Long id) {
//        this(id, null, null, null, null, Collections.emptyList());
//    }
}
