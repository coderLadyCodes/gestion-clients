package com.samia.gestion.clients.mapper;

import com.samia.gestion.clients.DTO.CareDTO;
import com.samia.gestion.clients.DTO.ProgramDTO;
import com.samia.gestion.clients.entity.Care;
import com.samia.gestion.clients.entity.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ProgramMapper {
    private final CareMapper careMapper;

    public ProgramMapper(CareMapper careMapper) {
        this.careMapper = careMapper;
    }
    public  Program mapToProgram(ProgramDTO programDTO) {
        List<Care> cares = programDTO.careDTOList().stream()
                .map(careMapper::mapToCare)
                .collect(Collectors.toList());
        return new Program(
                programDTO.id(),
                programDTO.userId(),
                programDTO.clientId(),
                programDTO.programReference(),
                programDTO.createdDate(),
                programDTO.totalProgramPrice(),
                cares
        );
    }

    public  ProgramDTO mapToProgramDTO(Program program){
        List<CareDTO> careDTOs = program.getCares().stream()
                .map(careMapper::mapToCareDTO)
                .collect(Collectors.toList());
        return new ProgramDTO(
                program.getId(),
                program.getUserId(),
                program.getClientId(),
                program.getProgramReference(),
                program.getCreatedDate(),
                program.getTotalProgramPrice(),
                careDTOs
        );
    }
}
