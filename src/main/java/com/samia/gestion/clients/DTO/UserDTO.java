package com.samia.gestion.clients.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.samia.gestion.clients.entity.Role;

public record UserDTO(
         Long id,
         String name,
         String email,
         String phone,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
         String password,
         Role role
) {}
