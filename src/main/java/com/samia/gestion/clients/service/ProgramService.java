package com.samia.gestion.clients.service;
import com.samia.gestion.clients.DTO.CareDTO;
import com.samia.gestion.clients.DTO.ProgramDTO;
import com.samia.gestion.clients.entity.*;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.OtherExceptions;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.mapper.ProgramMapper;
import com.samia.gestion.clients.repository.CareRepository;
import com.samia.gestion.clients.repository.ClientRepository;
import com.samia.gestion.clients.repository.ProductRepository;
import com.samia.gestion.clients.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final ClientRepository clientRepository;
    private final CareRepository careRepository;
    private final ProductRepository productRepository;

    public ProgramService(ProgramRepository programRepository, ProgramMapper programMapper, ClientRepository clientRepository, CareRepository careRepository, ProductRepository productRepository) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
        this.clientRepository = clientRepository;
        this.careRepository = careRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ProgramDTO createProgram(ProgramDTO programDTO){
        if (programDTO.clientId() == null){
            throw new OtherExceptions("le programme appartient à un client !");
        }

        Client client = clientRepository.findById(programDTO.clientId())
                .orElseThrow(() -> new NotFoundException("Client introuvable."));


        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }
        User userContext = (User) authentication.getPrincipal();
        Long userContextId = userContext.getId();

        List<Care> cares = careRepository.findByClientId(programDTO.clientId()).stream()
                .filter(care -> care.getProgram() == null)
                .collect(Collectors.toList());

        if (cares.isEmpty()) {
            throw new OtherExceptions("Aucun soin à associer au programme.");
        }

        Program program = programMapper.mapToProgram(programDTO);
        program.setUserId(userContextId);
        program.setClientId(client.getId());
        program.setCreatedDate(LocalDate.now());
        program.setCares(cares);
        BigDecimal totalProgramPrice = program.getCares().stream()
                .map(Care::getCarePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //.sum();
        program.setTotalProgramPrice(totalProgramPrice);

        programRepository.save(program);

        String programReference = "D-" + String.format("%03d", program.getId()); //change this
        program.setProgramReference(programReference);
        programRepository.save(program);

        cares.forEach(care -> {
            care.setProgram(program);
            careRepository.save(care);
        });
        // Optional: Explicitly flush after saving to ensure the IDs are set correctly
        careRepository.flush(); // Ensures all entities have their IDs set after persistence

        return programMapper.mapToProgramDTO(program);
    }

    public ProgramDTO getProgramById(Long id){
        if (id == null){
            throw new OtherExceptions("ID du programme ne peut pas etre null.");
        }

        Program program = programRepository.findById(id).orElseThrow(() -> new NotFoundException("Programme introuvable"));
        return programMapper.mapToProgramDTO(program);
    }

    @Transactional
    public ProgramDTO updateProgram(ProgramDTO programDetails, Long id) {
        System.out.println("Starting updateProgram method...");
        if (id == null) {
            throw new OtherExceptions("ID du programme ne peut pas être null.");
        }

        Program program = programRepository.findByIdWithCares(id)
                .orElseThrow(() -> new NotFoundException("Programme introuvable"));

        Client client = null;
        if (programDetails.clientId() != null) {
            client = clientRepository.findById(programDetails.clientId())
                    .orElseThrow(() -> new NotFoundException("Client introuvable"));
            program.setClientId(client != null ? client.getId() : null);
        }

        program.setCreatedDate(programDetails.createdDate() != null ? programDetails.createdDate() : LocalDate.now());
        if (programDetails.programReference() != null) {
            program.setProgramReference(programDetails.programReference());
        }
//        else {
//            program.setProgramReference(program.getProgramReference());
//        }
        program.setUserId(program.getUserId());

        List<CareDTO> careDTOList = programDetails.careDTOList() != null ? programDetails.careDTOList() : Collections.emptyList();

        List<Long> updatedCareIds = careDTOList.stream()
                .map(CareDTO::id)
                .collect(Collectors.toList());

        List<Care> caresToRemove = program.getCares().stream()
                .filter(care -> !updatedCareIds.contains(care.getId()))
                .collect(Collectors.toList());

        for (Care careToRemove : caresToRemove) {
            program.getCares().remove(careToRemove);
            careRepository.delete(careToRemove);
        }

        List<Care> caresToSave = new ArrayList<>();
        for (CareDTO careDTO : careDTOList) {
            Care care;
            if (careDTO.id() != null) {
                care = program.getCares().stream()
                        .filter(existingCare -> existingCare.getId() != null && existingCare.getId().equals(careDTO.id()))
                        .findFirst()
                        .orElse(new Care());
            } else {
                care = new Care();
            }
            Product product = productRepository.findById(careDTO.productDTO().id())
                    .orElseThrow(() -> new NotFoundException("Produit introuvable."));

            care.setProduct(product);
            care.setProgram(program);
            care.setClientId(careDTO.clientId());
            care.setUserId(careDTO.userId());
            care.setCarePrice(careDTO.carePrice());
            care.setQuantity(careDTO.quantity());
            care.setDurationWeeks(careDTO.durationWeeks());
            care.setTimeSlot(careDTO.timeSlot());
            care.setDaysOfWeek(careDTO.daysOfWeek());
            care.setModified( care.getId() == null ? LocalDate.now() : care.getCreated() );

            if (!program.getCares().contains(care)) {
                program.getCares().add(care);
            }
            caresToSave.add(care);
        }

        if (client != null && !client.getPrograms().contains(program)) {
            client.getPrograms().add(program);
        }

        if (!caresToSave.isEmpty()) {
            careRepository.saveAll(caresToSave);
            careRepository.flush(); // Ensures all entities have their IDs set after persistence
        }

        if (program.getCares().isEmpty()) {
            if (client != null) {
                client.getPrograms().remove(program);
            }
            programRepository.delete(program);
            return null;
        }
        BigDecimal totalProgramPrice = program.getCares().stream()
                .map(Care::getCarePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        //.sum();
        program.setTotalProgramPrice(totalProgramPrice);

        Program updatedProgram = programRepository.save(program);
        return programMapper.mapToProgramDTO(updatedProgram);
    }

//    @Transactional
//    public ProgramDTO updateProgram(ProgramDTO programDetails, Long id) {
//        System.out.println("Starting updateProgram method...");
//
//        if (id == null) {
//            System.out.println("Error: ID du programme est null.");
//            throw new OtherExceptions("ID du programme ne peut pas être null.");
//        }
//
//        System.out.println("Fetching program with ID: " + id);
//        Program program = programRepository.findByIdWithCares(id)
//                .orElseThrow(() -> new NotFoundException("Programme introuvable"));
//
//        // Log program details
//        System.out.println("Program found: " + program);
//
//        Client client = null;
//        if (programDetails.clientId() != null) {
//            System.out.println("Fetching client with ID: " + programDetails.clientId());
//            client = clientRepository.findById(programDetails.clientId())
//                    .orElseThrow(() -> new NotFoundException("Client introuvable"));
//            program.setClientId(client != null ? client.getId() : null);
//            System.out.println("Client set for program: " + client);
//        }
//
//        program.setCreatedDate(programDetails.createdDate() != null ? programDetails.createdDate() : LocalDate.now());
//        System.out.println("Program created date set to: " + program.getCreatedDate());
//
//        if (programDetails.programReference() != null) {
//            program.setProgramReference(programDetails.programReference());
//            System.out.println("Program reference set to: " + programDetails.programReference());
//        }
//        program.setUserId(program.getUserId());
//
//        List<CareDTO> careDTOList = programDetails.careDTOList() != null ? programDetails.careDTOList() : Collections.emptyList();
//        System.out.println("Care DTO list size: " + careDTOList.size());
//
//        List<Long> updatedCareIds = careDTOList.stream()
//                .map(CareDTO::id)
//                .collect(Collectors.toList());
//        System.out.println("Updated care IDs: " + updatedCareIds);
//
//        List<Care> caresToRemove = program.getCares().stream()
//                .filter(care -> !updatedCareIds.contains(care.getId()))
//                .collect(Collectors.toList());
//        System.out.println("Cares to remove: " + caresToRemove.size());
//
//        for (Care careToRemove : caresToRemove) {
//            System.out.println("Removing care with ID: " + careToRemove.getId());
//            program.getCares().remove(careToRemove);
//            careRepository.delete(careToRemove);
//        }
//
//        List<Care> caresToSave = new ArrayList<>();
//        for (CareDTO careDTO : careDTOList) {
//            System.out.println("Processing CareDTO: " + careDTO);
//            Care care;
//            if (careDTO.id() != null) {
//                care = program.getCares().stream()
//                        .filter(existingCare -> existingCare.getId() != null && existingCare.getId().equals(careDTO.id()))
//                        .findFirst()
//                        .orElse(new Care());
//                System.out.println("Found existing care with ID: " + careDTO.id());
//            } else {
//                care = new Care();
//                System.out.println("Creating new care.");
//            }
//            Product product = productRepository.findById(careDTO.productDTO().id())
//                    .orElseThrow(() -> new NotFoundException("Produit introuvable."));
//            System.out.println("Found product with ID: " + careDTO.productDTO().id());
//
//            care.setProduct(product);
//            care.setProgram(program);
//            care.setClientId(careDTO.clientId());
//            care.setUserId(careDTO.userId());
//            care.setCarePrice(careDTO.carePrice());
//            care.setQuantity(careDTO.quantity());
//            care.setDurationWeeks(careDTO.durationWeeks());
//            care.setTimeSlot(careDTO.timeSlot());
//            care.setDaysOfWeek(careDTO.daysOfWeek());
//            care.setModified(care.getId() == null ? LocalDate.now() : care.getCreated());
//
//            if (!program.getCares().contains(care)) {
//                program.getCares().add(care);
//            }
//            caresToSave.add(care);
//        }
//
//        System.out.println("Saving new/updated care entries.");
//        if (client != null && !client.getPrograms().contains(program)) {
//            client.getPrograms().add(program);
//        }
//
//        if (!caresToSave.isEmpty()) {
//            careRepository.saveAll(caresToSave);
//            careRepository.flush(); // Ensures all entities have their IDs set after persistence
//            System.out.println("Cares saved and flushed.");
//        }
//
//        if (program.getCares().isEmpty()) {
//            if (client != null) {
//                client.getPrograms().remove(program);
//            }
//            System.out.println("No cares left, deleting program with ID: " + id);
//            programRepository.delete(program);
//            return null;
//        }
//
//        BigDecimal totalProgramPrice = program.getCares().stream()
//                .map(Care::getCarePrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        program.setTotalProgramPrice(totalProgramPrice);
//        System.out.println("Total program price calculated: " + totalProgramPrice);
//
//        Program updatedProgram = programRepository.save(program);
//        System.out.println("Program updated with ID: " + updatedProgram.getId());
//
//        return programMapper.mapToProgramDTO(updatedProgram);
//    }
//

    public List<ProgramDTO> getAllProgramsByClientId(Long clientId) {
        if (clientId == null) {
            throw new OtherExceptions("L'ID ne peut pas être null.");
        }
        List<Program> programs = programRepository.findAllByClientId(clientId);
        return programs.isEmpty()
                ? Collections.emptyList()
                : programs.stream()
                .map(programMapper::mapToProgramDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProgram(Long id){
        if (id == null) {
            throw new NotFoundException("L'ID du programme ne peut pas être null.");
        }
        if (!programRepository.existsById(id)) {
            throw new NotFoundException("ce programme avec n'existe pas.");
        }
        programRepository.deleteById(id);
    }
}
