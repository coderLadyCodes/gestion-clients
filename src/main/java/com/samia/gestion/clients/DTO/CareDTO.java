package com.samia.gestion.clients.DTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record CareDTO(
        Long id,
        Long clientId,
        Long userId,
        ProductDTO productDTO,
        Long programId,
        double carePrice,
        int quantity,
        int durationWeeks,
        @JsonProperty("timeSlot")
        List<String> timeSlot,
        @JsonProperty("daysOfWeek")
        List<DayOfWeek> daysOfWeek,
        LocalDate created,
        LocalDate modified
) { }
