package com.samia.gestion.clients.mapper;

import com.samia.gestion.clients.DTO.CareDTO;
import com.samia.gestion.clients.DTO.ProductDTO;
import com.samia.gestion.clients.entity.Care;
import com.samia.gestion.clients.entity.Product;
import com.samia.gestion.clients.entity.Program;
import org.springframework.stereotype.Component;

@Component
public class CareMapper {
    private final ProductMapper productMapper;
    public CareMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public Care mapToCare(CareDTO careDTO){
        Product product = productMapper.mapToProduct(careDTO.productDTO());
        return new Care(
                careDTO.id(),
                careDTO.clientId(),
                careDTO.userId(),
                product,
                new Program(careDTO.programId()),
                careDTO.carePrice(),
                careDTO.quantity(),
                careDTO.durationWeeks(),
                careDTO.timeSlot(),
                careDTO.daysOfWeek(),
                careDTO.created(),
                careDTO.modified()
        );
    }

    public  CareDTO mapToCareDTO(Care care){
        ProductDTO productDTO = ProductMapper.mapToProductDTO(care.getProduct());
        Long programId = care.getProgram() != null ? care.getProgram().getId() : null;
        return new CareDTO(
                care.getId(),
                care.getClientId(),
                care.getUserId(),
                productDTO,
                programId,
                care.getCarePrice(),
                care.getQuantity(),
                care.getDurationWeeks(),
                care.getTimeSlot(),
                care.getDaysOfWeek(),
                care.getCreated(),
                care.getModified()
        );
    }
}
