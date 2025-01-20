package com.samia.gestion.clients.DTO;

public record ProductDTO(
        Long id,
        Long userId,
        String type,
        String name,
        String refProduct,
        String description,
        CategoryDTO categoryDTO,
        double productPrice
) { }
