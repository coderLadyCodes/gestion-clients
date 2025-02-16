package com.samia.gestion.clients.DTO;

import com.samia.gestion.clients.entity.Tva;

public record CategoryDTO(
        Long id,
        Long userId,
        String name,
        //Tva tva
        String tva
) {
    }

