package com.samia.gestion.clients.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public record PaginationResponse<T>(
        @JsonProperty("content") List<T> content,
        @JsonProperty("currentPage") int currentPage,
        @JsonProperty("totalPages") int totalPages,
        @JsonProperty("totalElements") Long totalElements
) { }
