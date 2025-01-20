package com.samia.gestion.clients.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.samia.gestion.clients.DTO.ProgramDTO;
import com.samia.gestion.clients.service.ProgramService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProgramController {
    public final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping("/program")
    public ProgramDTO createProgram(@RequestBody ProgramDTO programDTO){
        System.out.println("***************Program Created CONTROLLER**************");
      return programService.createProgram(programDTO) ;
    }

    @GetMapping("/program/{id}")
    public ProgramDTO getProgramById(@PathVariable(value = "id") Long id){
        return programService.getProgramById(id);
    }

//    @PutMapping("/program/update/{id}")
//    public ProgramDTO updateProgram(@RequestBody ProgramDTO programDetails, @PathVariable(value = "id") Long id){
//        System.out.println("***************Received Program Update Request**************");
//        System.out.println("***********************Payload: " + programDetails);
//        return programService.updateProgram(programDetails, id);
//    }

    @PutMapping("/program/update/{id}")
    public ProgramDTO updateProgram(@RequestBody String rawPayload, @PathVariable Long id) throws JsonProcessingException {
    System.out.println("Raw JSON Payload: " + rawPayload);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    ProgramDTO programDetails = mapper.readValue(rawPayload, ProgramDTO.class);
    System.out.println("Deserialized ProgramDTO: " + programDetails);
    return programService.updateProgram(programDetails, id);
}


    @GetMapping("/programs/{clientId}")
    public List<ProgramDTO> getAllProgramsByClientId(@PathVariable(value = "clientId") Long clientId){
        return programService.getAllProgramsByClientId(clientId);
    }

    @DeleteMapping("/program/{id}")
    public void deleteProgram(@PathVariable(value = "id") Long id){
        programService.deleteProgram(id);
    }
}
