package com.samia.gestion.clients.controller;

import com.samia.gestion.clients.DTO.CareDTO;
import com.samia.gestion.clients.service.CareService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CareController {
    private final CareService careService;

    public CareController(CareService careService) {
        this.careService = careService;
    }
    @PostMapping("/care")
    public CareDTO createCare(@RequestBody CareDTO careDTO){
        System.out.println("****************CONTROLLER CREATE CARE***************" );
        return careService.createCare(careDTO);
    }

    @GetMapping("/care/{id}")
    public CareDTO getCareById(@PathVariable(value = "id") Long id){
        return careService.getCareById(id);
        }

    @PutMapping("/care/{id}")
    public  CareDTO updateCare(@RequestBody CareDTO carDetails,  @PathVariable(value = "id") Long id){
        System.out.println("****************CONTROLLER UPDATE CARE***************" );
        return  careService.updateCare(carDetails,id);
    }

    @GetMapping("/cares/{clientId}")
    public List<CareDTO> getAllCaresByClientId(@PathVariable(value = "clientId") Long clientId){
        return careService.getAllCaresByClientId(clientId);
    }

    @DeleteMapping("/care/{id}")
    public void deleteCare(@PathVariable(value = "id") Long id){
         careService.deleteCare(id);
    }
}
