package com.samia.gestion.clients.service;

import com.samia.gestion.clients.DTO.CategoryDTO;
import com.samia.gestion.clients.entity.Category;
import com.samia.gestion.clients.entity.Tva;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.AlreadyExistsException;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.OtherExceptions;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.repository.CategoryRepository;
import com.samia.gestion.clients.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Category mapToCatygory(CategoryDTO categoryDTO){
        Tva tvaEnum = (categoryDTO.tva() != null) ? Tva.fromValue(categoryDTO.tva()) : null;
        return new Category(
                categoryDTO.id(),
                categoryDTO.userId(),
                categoryDTO.name(),
                //categoryDTO.tva()
                tvaEnum
        );
    }
    public CategoryDTO maptoCategoryDTO(Category category){
        String tvaValue = (category.getTva() != null) ? category.getTva().getValue() : null;
        return new CategoryDTO(
                category.getId(),
                category.getUserId(),
                category.getName(),
                //category.getTva()
                tvaValue
        );
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO){
        if (categoryDTO.name() == null || categoryDTO.name().trim().isEmpty()){
            throw  new OtherExceptions("Ce champ est obligatoire");
        }

        Category category = mapToCatygory(categoryDTO);
        Optional<Category> existingCategory = categoryRepository.findByName(categoryDTO.name());
        if(existingCategory.isPresent()){
            throw  new AlreadyExistsException("Cette Catégorie existe déja");
        }

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }
        User userContext = (User) authentication.getPrincipal();
        Long userContextId = userContext.getId();
        category.setUserId(userContextId);
        Category savedCategory = categoryRepository.save(category);
        return maptoCategoryDTO(savedCategory);
    }

    public CategoryDTO getCategoryById(Long id){
        if(id == null){
            throw new OtherExceptions("Id de la categorie ne peut pas etre null.");
        }
        Category category = categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("catégorie non trouvée."));
        return maptoCategoryDTO(category);
    }

    public CategoryDTO getCategoryByName(String name){
        if(name.isEmpty() || name.trim().isEmpty()){
            throw new OtherExceptions("Le nom de la catégorie ne peut pas être vide.");
        }
        Category category = categoryRepository.findByName(name).orElseThrow(()-> new NotFoundException("Le nom de la categorie '\" + name + \"' est introuvable."));
        return maptoCategoryDTO(category);
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDetails, Long id){
        if(id == null){
            throw new OtherExceptions("Id de la categorie ne peut pas etre null.");
        }
        if(categoryDetails.name() == null || categoryDetails.name().isEmpty()){
            throw  new OtherExceptions("Ce champ est obligatoire.");
        }

        Category category = categoryRepository.findById(id).orElseThrow(()-> new NotFoundException("Catégorie non trouvée avec ID : " + id));
        if(!category.getName().equals(categoryDetails.name())){
            if (categoryRepository.findByName(categoryDetails.name()).isPresent()){
                throw new AlreadyExistsException("cette catégorie existe déja.");
            }
        }
        category.setName(categoryDetails.name() != null ? categoryDetails.name() : category.getName());
        //category.setTva(categoryDetails.tva() != null ? categoryDetails.tva() : category.getTva());
//        // Check if "Aucune TVA" is selected in the frontend
//        if ("".equals(categoryDetails.tva())) {
//            category.setTva(null);  // Set to null if "Aucune TVA" is selected
//        } else {
//            category.setTva(categoryDetails.tva() != null ? categoryDetails.tva() : category.getTva());
//        }

        // Update the tva
        if (categoryDetails.tva() != null) {
            category.setTva(Tva.fromValue(categoryDetails.tva())); // Convert string to enum
        }

        Category updatedCategory = categoryRepository.save(category);
        return maptoCategoryDTO(updatedCategory);
    }

    public List<CategoryDTO> getAllCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.isEmpty()){
            return Collections.emptyList();
        }
        return categoryList.stream().map(this::maptoCategoryDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteCategory(Long id){
        if(id == null){
            throw new OtherExceptions("Id de la categorie ne peut pas etre null.");
        }
        productRepository.setCategoryToNull(id);
        categoryRepository.deleteById(id);
    }

}
