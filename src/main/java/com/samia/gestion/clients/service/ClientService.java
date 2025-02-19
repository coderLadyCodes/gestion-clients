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
                programs,
                clientDTO.stress(),
                clientDTO.stressPsychoaffectif(),
                clientDTO.fumezVousCombien(),
                clientDTO.dereglementHormonal(),
                clientDTO.crisesEpilepsie(),
                clientDTO.interventionsChirurgicales(),
                clientDTO.pacemaker(),
                clientDTO.anticoagulants(),
                clientDTO.allergiesOuIntolerances(),
                clientDTO.problemesThyroidiens(),
                clientDTO.chimiotherapie(),
                clientDTO.fatigueGenerale(),
                clientDTO.regimeAlimentaire(),
                clientDTO.carences(),
                clientDTO.gastroplastie(),
                clientDTO.menopause(),
                clientDTO.moyenContraception(),
                clientDTO.reglesAbondantes(),
                clientDTO.enceinte(),
                clientDTO.enfant2DerniereAnnee(),
                clientDTO.chuteApresAccouchement(),
                clientDTO.interruptionAllaitement(),
                clientDTO.fausseCouche(),
                clientDTO.herediteChuteCheveux(),
                clientDTO.calvitie(),
                clientDTO.cheveuxPeuAbondants(),
                clientDTO.pelade(),
                clientDTO.cheveuxGras(),
                clientDTO.cheveuxSecs(),
                clientDTO.chuteCheveuxDepuisQuand(),
                clientDTO.typeChute(),
                clientDTO.trichotillomanie(),
                clientDTO.nombreShampoingsParSemaine(),
                clientDTO.apresShampoingMasque(),
                clientDTO.methodesAgressivesCoiffage(),
                clientDTO.alimentation()
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
                programDTOList,
                client.getStress(),
                client.getStressPsychoaffectif(),
                client.getFumezVousCombien(),
                client.getDereglementHormonal(),
                client.getCrisesEpilepsie(),
                client.getInterventionsChirurgicales(),
                client.getPacemaker(),
                client.getAnticoagulants(),
                client.getAllergiesOuIntolerances(),
                client.getProblemesThyroidiens(),
                client.getChimiotherapie(),
                client.getFatigueGenerale(),
                client.getRegimeAlimentaire(),
                client.getCarences(),
                client.getGastroplastie(),
                client.getMenopause(),
                client.getMoyenContraception(),
                client.getReglesAbondantes(),
                client.getEnceinte(),
                client.getEnfant2DerniereAnnee(),
                client.getChuteApresAccouchement(),
                client.getInterruptionAllaitement(),
                client.getFausseCouche(),
                client.getHerediteChuteCheveux(),
                client.getCalvitie(),
                client.getCheveuxPeuAbondants(),
                client.getPelade(),
                client.getCheveuxGras(),
                client.getCheveuxSecs(),
                client.getChuteCheveuxDepuisQuand(),
                client.getTypeChute(),
                client.getTrichotillomanie(),
                client.getNombreShampoingsParSemaine(),
                client.getApresShampoingMasque(),
                client.getMethodesAgressivesCoiffage(),
                client.getAlimentation()
        );
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO){
//    if (clientDTO.firstName() == null || clientDTO.lastName() == null || clientDTO.email() == null) {
//        throw new OtherExceptions( "Les champs sont obligatoires.");
//    }
    Client client = mapToClient(clientDTO);
//        Optional<Client> existingClient = clientRepository.findByEmail(clientDTO.email());
//        if(existingClient.isPresent()){
//            throw new AlreadyExistsException("Utilisateur existe déja");
//        }
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
        // Handling new fields
        client.setStress(clientDetails.stress() != null ? clientDetails.stress() : client.getStress());
        client.setStressPsychoaffectif(clientDetails.stressPsychoaffectif() != null ? clientDetails.stressPsychoaffectif() : client.getStressPsychoaffectif());
        client.setFumezVousCombien(clientDetails.fumezVousCombien() != null ? clientDetails.fumezVousCombien() : client.getFumezVousCombien());
        client.setDereglementHormonal(clientDetails.dereglementHormonal() != null ? clientDetails.dereglementHormonal() : client.getDereglementHormonal());
        client.setCrisesEpilepsie(clientDetails.crisesEpilepsie() != null ? clientDetails.crisesEpilepsie() : client.getCrisesEpilepsie());
        client.setInterventionsChirurgicales(clientDetails.interventionsChirurgicales() != null ? clientDetails.interventionsChirurgicales() : client.getInterventionsChirurgicales());
        client.setPacemaker(clientDetails.pacemaker() != null ? clientDetails.pacemaker() : client.getPacemaker());
        client.setAnticoagulants(clientDetails.anticoagulants() != null ? clientDetails.anticoagulants() : client.getAnticoagulants());
        client.setAllergiesOuIntolerances(clientDetails.allergiesOuIntolerances() != null ? clientDetails.allergiesOuIntolerances() : client.getAllergiesOuIntolerances());
        client.setProblemesThyroidiens(clientDetails.problemesThyroidiens() != null ? clientDetails.problemesThyroidiens() : client.getProblemesThyroidiens());
        client.setChimiotherapie(clientDetails.chimiotherapie() != null ? clientDetails.chimiotherapie() : client.getChimiotherapie());
        client.setFatigueGenerale(clientDetails.fatigueGenerale() != null ? clientDetails.fatigueGenerale() : client.getFatigueGenerale());
        client.setRegimeAlimentaire(clientDetails.regimeAlimentaire() != null ? clientDetails.regimeAlimentaire() : client.getRegimeAlimentaire());
        client.setCarences(clientDetails.carences() != null ? clientDetails.carences() : client.getCarences());
        client.setGastroplastie(clientDetails.gastroplastie() != null ? clientDetails.gastroplastie() : client.getGastroplastie());
        client.setMenopause(clientDetails.menopause() != null ? clientDetails.menopause() : client.getMenopause());
        client.setMoyenContraception(clientDetails.moyenContraception() != null ? clientDetails.moyenContraception() : client.getMoyenContraception());
        client.setReglesAbondantes(clientDetails.reglesAbondantes() != null ? clientDetails.reglesAbondantes() : client.getReglesAbondantes());
        client.setEnceinte(clientDetails.enceinte() != null ? clientDetails.enceinte() : client.getEnceinte());
        client.setEnfant2DerniereAnnee(clientDetails.enfant2DerniereAnnee() != null ? clientDetails.enfant2DerniereAnnee() : client.getEnfant2DerniereAnnee());
        client.setChuteApresAccouchement(clientDetails.chuteApresAccouchement() != null ? clientDetails.chuteApresAccouchement() : client.getChuteApresAccouchement());
        client.setInterruptionAllaitement(clientDetails.interruptionAllaitement() != null ? clientDetails.interruptionAllaitement() : client.getInterruptionAllaitement());
        client.setFausseCouche(clientDetails.fausseCouche() != null ? clientDetails.fausseCouche() : client.getFausseCouche());
        client.setHerediteChuteCheveux(clientDetails.herediteChuteCheveux() != null ? clientDetails.herediteChuteCheveux() : client.getHerediteChuteCheveux());
        client.setCalvitie(clientDetails.calvitie() != null ? clientDetails.calvitie() : client.getCalvitie());
        client.setCheveuxPeuAbondants(clientDetails.cheveuxPeuAbondants() != null ? clientDetails.cheveuxPeuAbondants() : client.getCheveuxPeuAbondants());
        client.setPelade(clientDetails.pelade() != null ? clientDetails.pelade() : client.getPelade());
        client.setCheveuxGras(clientDetails.cheveuxGras() != null ? clientDetails.cheveuxGras() : client.getCheveuxGras());
        client.setCheveuxSecs(clientDetails.cheveuxSecs() != null ? clientDetails.cheveuxSecs() : client.getCheveuxSecs());
        client.setChuteCheveuxDepuisQuand(clientDetails.chuteCheveuxDepuisQuand() != null ? clientDetails.chuteCheveuxDepuisQuand() : client.getChuteCheveuxDepuisQuand());
        client.setTypeChute(clientDetails.typeChute() != null ? clientDetails.typeChute() : client.getTypeChute());
        client.setTrichotillomanie(clientDetails.trichotillomanie() != null ? clientDetails.trichotillomanie() : client.getTrichotillomanie());
        client.setNombreShampoingsParSemaine(clientDetails.nombreShampoingsParSemaine() != null ? clientDetails.nombreShampoingsParSemaine() : client.getNombreShampoingsParSemaine());
        client.setApresShampoingMasque(clientDetails.apresShampoingMasque() != null ? clientDetails.apresShampoingMasque() : client.getApresShampoingMasque());
        client.setMethodesAgressivesCoiffage(clientDetails.methodesAgressivesCoiffage() != null ? clientDetails.methodesAgressivesCoiffage() : client.getMethodesAgressivesCoiffage());
        client.setAlimentation(clientDetails.alimentation() != null ? clientDetails.alimentation() : client.getAlimentation());


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
