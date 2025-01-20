package com.samia.gestion.clients.controller;

import com.samia.gestion.clients.DTO.ClientDTO;
import com.samia.gestion.clients.DTO.PaginationResponse;
import com.samia.gestion.clients.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client")
    public ClientDTO createClient(@Valid @RequestBody ClientDTO clientDTO){
        return clientService.createClient(clientDTO);
    }

    @GetMapping("/client/{id}")
    public ClientDTO getClientById(@PathVariable(value = "id") Long id){
        return clientService.getClientById(id);
    }

    @PutMapping("/client/{id}")
    public ClientDTO updateClient(@RequestBody ClientDTO clientDetails, @PathVariable(value = "id") Long id){
        return clientService.updateClient(clientDetails, id);
    }

//    @GetMapping("/clients")
//    public List<ClientDTO> getAllClients(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
//                                         @RequestParam(value = "size", defaultValue = "20", required = false) int size){
//        return clientService.getAllClients(page,size); //NOT SURE FOR PAGINATION
//    }

    @GetMapping("/clients")
    @ResponseBody
    public PaginationResponse<ClientDTO> getAllClients(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                       @RequestParam(value = "size", defaultValue = "20", required = false) int size,
                                                       @RequestParam(value = "search", required = false) String search){
        return clientService.getAllClients(page,size,search);
    }

    @DeleteMapping("/client/{id}")
    public void deleteClient(@PathVariable(value = "id") Long id){
        clientService.deleteClient(id);
    }
}
