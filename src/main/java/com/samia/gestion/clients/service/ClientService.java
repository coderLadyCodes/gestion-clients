package com.samia.gestion.clients.service;

import com.samia.gestion.clients.DTO.ClientDTO;
import com.samia.gestion.clients.DTO.PaginationResponse;
import com.samia.gestion.clients.DTO.ProgramDTO;
import com.samia.gestion.clients.entity.Client;
import com.samia.gestion.clients.entity.Program;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.AlreadyExistsException;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.OtherExceptions;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.mapper.ProgramMapper;
import com.samia.gestion.clients.repository.ClientRepository;
import com.samia.gestion.clients.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ProgramMapper programMapper;
    private final ProgramRepository programRepository;

    public ClientService(ClientRepository clientRepository, ProgramMapper programMapper, ProgramRepository programRepository) {
        this.clientRepository = clientRepository;
        this.programMapper = programMapper;
        this.programRepository = programRepository;
    }

    public Client mapToClient(ClientDTO clientDTO){
        List<Program> programs = clientDTO.programDTOS()  != null ?
                clientDTO.programDTOS().stream()
                .map(programMapper::mapToProgram)
                .collect(Collectors.toList())
                : new ArrayList<>();
        return new Client(
                clientDTO.id(),
                clientDTO.firstName(),
                clientDTO.lastName(),
                clientDTO.comments(),
                clientDTO.email(),
                clientDTO.mobilePhone(),
                clientDTO.homePhone(),
                clientDTO.birthday(),
                clientDTO.city(),
                clientDTO.streetName(),
                clientDTO.zipCode(),
                clientDTO.created(),
                clientDTO.modified(),
                clientDTO.sex(),
                clientDTO.userId(),
                programs
        );
    }
    public ClientDTO mapToClientDTO(Client client){
        List<ProgramDTO> programDTOList = client.getPrograms() != null ?
                client.getPrograms().stream()
                .map(programMapper::mapToProgramDTO)
                .collect(Collectors.toList())
                : new ArrayList<>();
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getComments(),
                client.getEmail(),
                client.getMobilePhone(),
                client.getHomePhone(),
                client.getBirthday(),
                client.getCity(),
                client.getStreetName(),
                client.getZipCode(),
                client.getCreated(),
                client.getModified(),
                client.getSex(),
                client.getUserId(),
                programDTOList
        );
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO){
//    if (clientDTO.firstName() == null || clientDTO.lastName() == null || clientDTO.email() == null) {
//        throw new OtherExceptions( "Les champs sont obligatoires.");
//    }
    Client client = mapToClient(clientDTO);
        Optional<Client> existingClient = clientRepository.findByEmail(clientDTO.email());
        if(existingClient.isPresent()){
            throw new AlreadyExistsException("l'adresse mail est déja utilisée");
        }
        if (clientDTO.email() != null && clientDTO.email().trim().isEmpty()) {
            client.setEmail(null);
        }
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }
        User userContext = (User) authentication.getPrincipal();
        Long userContextId = userContext.getId();
        client.setUserId(userContextId);
        client.setCreated(LocalDate.now());
        client.setModified(LocalDate.now());
        client.setPrograms(new ArrayList<>());
        Client savedClient = clientRepository.save(client);
        return mapToClientDTO(savedClient);
    }

    public ClientDTO getClientById(Long id){
      if(id == null){
          throw new OtherExceptions("Id cannot be null");
      }
      Client client = clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client introuvable"));
      return mapToClientDTO(client);
    }

    @Transactional
    public ClientDTO updateClient(ClientDTO clientDetails, Long id){
        if(id == null){
            throw new OtherExceptions("ID du client ne peut pas etre null.");
        }
//        if (clientDetails.firstName() == null || clientDetails.lastName() == null || clientDetails.email() == null) {
//            throw new OtherExceptions("Les champs sont obligatoires.");
//        }

        Client client = clientRepository.findById(id).orElseThrow(()-> new NotFoundException("Client introuvable"));
        if(!client.getEmail().equals(clientDetails.email())){
            Optional<Client> optionalClient = clientRepository.findByEmail(clientDetails.email());
            if(optionalClient.isPresent()){
                throw new AlreadyExistsException("cette adresse mail est déja utilisé.");
            }
        }

        client.setFirstName(clientDetails.firstName() != null ? clientDetails.firstName() : client.getFirstName());
        client.setLastName(clientDetails.lastName() != null ? clientDetails.lastName() : client.getLastName());
        client.setComments(clientDetails.comments() != null ? clientDetails.comments() : client.getComments());
        client.setEmail(clientDetails.email() != null ? clientDetails.email() : client.getEmail());
        client.setMobilePhone(clientDetails.mobilePhone() != null ? clientDetails.mobilePhone() : client.getMobilePhone());
        client.setHomePhone(clientDetails.homePhone() != null ? clientDetails.homePhone() : client.getHomePhone());
        client.setBirthday(clientDetails.birthday() != null ? clientDetails.birthday() : client.getBirthday());
        client.setCity(clientDetails.city() != null ? clientDetails.city() : client.getCity());
        client.setStreetName(clientDetails.streetName() != null ? clientDetails.streetName() : client.getStreetName());
        client.setZipCode(clientDetails.zipCode() != null ? clientDetails.zipCode() : client.getZipCode());
        client.setSex(clientDetails.sex() != null ? clientDetails.sex() : client.getSex());
        client.setModified(LocalDate.now());

        if(clientDetails.programDTOS() != null){
            List<Program> updatedPrograms = clientDetails.programDTOS().stream()
                    .map(programMapper::mapToProgram)
                    .collect(Collectors.toList());
            client.setPrograms(updatedPrograms);
        }
        Client updatedClient = clientRepository.save(client);
        return mapToClientDTO(updatedClient);
    }

  public PaginationResponse<ClientDTO> getAllClients(int page, int size, String search){
      PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        //Page<Client> clientPage = clientRepository.searchClient(search, pageRequest);
      // to handle empty search properly
      Page<Client> clientPage;
      if (search == null || search.trim().isEmpty()) {
          clientPage = clientRepository.findAll(pageRequest); // Return all clients if no search
      } else {
          clientPage = clientRepository.searchClient(search, pageRequest);
      }

        List<ClientDTO> clientDTOs =  clientPage.getContent().stream()
                .map(this::mapToClientDTO)
                .collect(Collectors.toList());

        return new PaginationResponse<>(
                clientDTOs,
                page,
                clientPage.getTotalPages(),
                clientPage.getTotalElements()
        );
    }

    @Transactional
    public void deleteClient(Long clientId){
        if(clientId == null){
            throw new OtherExceptions("L'ID du client ne peut pas être null.");
        }
        Client client = clientRepository.findById(clientId).orElseThrow(()-> new NotFoundException("Client introuvable"));
        programRepository.deleteAllByClientId(clientId);
        clientRepository.delete(client);
    }
}
