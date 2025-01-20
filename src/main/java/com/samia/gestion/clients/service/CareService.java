package com.samia.gestion.clients.service;

import com.samia.gestion.clients.DTO.CareDTO;
import com.samia.gestion.clients.entity.*;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.OtherExceptions;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.mapper.CareMapper;
import com.samia.gestion.clients.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareService {
    private final CareRepository careRepository;
    private final CareMapper careMapper;
    private final ProductRepository productRepository;
    private final ProgramRepository programRepository;
    private final ClientRepository clientRepository;

    public CareService(CareRepository careRepository, CareMapper careMapper, ProductRepository productRepository, ProgramRepository programRepository, ClientRepository clientRepository) {
        this.careRepository = careRepository;
        this.careMapper = careMapper;
        this.productRepository = productRepository;
        this.programRepository = programRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public CareDTO createCare(CareDTO careDTO){
        if (careDTO.clientId() == null){
            throw new OtherExceptions("le soin appartient à un client !");
        }
        Client client = clientRepository.findById(careDTO.clientId()).orElseThrow(()-> new NotFoundException("Client introuvable."));

        Product product = productRepository.findById(careDTO.productDTO().id()).orElseThrow(()-> new NotFoundException("produit introuvable."));

        Program program = null;
        if (careDTO.programId() != null) {
            program = programRepository.findById(careDTO.programId())
                    .orElseThrow(() -> new NotFoundException("Programme introuvable."));
        }

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }
        User userContext = (User) authentication.getPrincipal();
        Long userContextId = userContext.getId();

        if (careDTO.quantity() < 0) {
            throw new OtherExceptions("La quantité ne peut pas être négative.");
        }

        if (careDTO.carePrice() < 0) {
            throw new OtherExceptions("Le prix du soin ne peut pas être négatif.");
        }

        double calculatedCarePrice = product.getProductPrice() * careDTO.quantity();

        Care care = careMapper.mapToCare(careDTO);
        care.setProduct(product);
        care.setClientId(client.getId());
        care.setUserId(userContextId);
        care.setCreated(LocalDate.now());
        care.setModified(LocalDate.now());
        care.setCarePrice(calculatedCarePrice);
        care.setProgram(program);
        care.setTimeSlot(careDTO.timeSlot() != null ? careDTO.timeSlot() : new ArrayList<>());
        care.setDaysOfWeek(careDTO.daysOfWeek() != null ? careDTO.daysOfWeek() : new ArrayList<>());
        care.setDurationWeeks(careDTO.durationWeeks());
        Care savedCare = careRepository.save(care);
        return careMapper.mapToCareDTO(savedCare);
    }

    public CareDTO getCareById(Long id){
        if (id == null){
            throw new OtherExceptions("ID du soins ne peut pas etre null.");
        }
        Care care = careRepository.findById(id).orElseThrow(()-> new NotFoundException("le soin est introuvable."));
        return careMapper.mapToCareDTO(care);
    }

    @Transactional
    public CareDTO updateCare(CareDTO careDetails, Long id){
        if (id == null){
            throw new OtherExceptions("ID du soins ne peut pas etre null.");
        }

        Care existingCare = careRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Soin introuvable."));

            Product product = careDetails.productDTO() != null
                    ? productRepository.findById(careDetails.productDTO().id())
                    .orElseThrow(() -> new NotFoundException("Produit introuvable."))
                    : existingCare.getProduct();

        if (careDetails.quantity() < 0) {
            throw new OtherExceptions("La quantité ne peut pas être négative.");
        }
        if (careDetails.carePrice() < 0) {
            throw new OtherExceptions("Le prix du soin ne peut pas être négatif.");
        }
        double calculatedCarePrice = product.getProductPrice() * careDetails.quantity();

        Program program = careDetails.programId() != null
                ? programRepository.findById(careDetails.programId())
                .orElseThrow(() -> new NotFoundException("Programme introuvable."))
                : programRepository.findFirstByClientId(careDetails.clientId())
                .orElseGet(() -> {
                    Program newProgram = new Program();
                    newProgram.setClientId(careDetails.clientId());
                    newProgram.setUserId(careDetails.userId());
                    newProgram.setCreatedDate(LocalDate.now());
                    return programRepository.save(newProgram);
                });
        if (!existingCare.getProgram().equals(program)) {
            existingCare.setProgram(program);
        }
        existingCare.setClientId(careDetails.clientId());
        existingCare.setUserId(careDetails.userId());
        existingCare.setCarePrice(calculatedCarePrice);
        existingCare.setQuantity(careDetails.quantity());
        existingCare.setDurationWeeks(careDetails.durationWeeks());
        existingCare.setTimeSlot(careDetails.timeSlot() != null ? careDetails.timeSlot() : new ArrayList<>());
        existingCare.setDaysOfWeek(careDetails.daysOfWeek() != null ? careDetails.daysOfWeek() : new ArrayList<>());
        existingCare.setModified(LocalDate.now());

        Care updatedCare = careRepository.save(existingCare);
        return careMapper.mapToCareDTO(updatedCare);
    }

    @Transactional
    public List<CareDTO> getAllCaresByClientId(Long clientId){
        if (clientId == null) {
            throw new OtherExceptions("L'ID du client ne peut pas être null.");
        }

        List<Care> cares = careRepository.findByClientId(clientId);

        if (cares.isEmpty()) {
            throw new NotFoundException("Aucun soin trouvé.");
        }
        return cares.stream()
                .map(careMapper::mapToCareDTO)
                .collect(Collectors.toList());
    }

    public void deleteCare(Long id){
        if (id == null){
            throw new OtherExceptions("ID du soins ne peut pas etre null.");
        }
        careRepository.deleteById(id);
    }
}
