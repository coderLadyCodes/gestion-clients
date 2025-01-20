package com.samia.gestion.clients.service;

import com.samia.gestion.clients.DTO.PaginationResponse;
import com.samia.gestion.clients.DTO.ProductDTO;
import com.samia.gestion.clients.entity.Care;
import com.samia.gestion.clients.entity.Category;
import com.samia.gestion.clients.entity.Product;
import com.samia.gestion.clients.entity.User;
import com.samia.gestion.clients.exception.AlreadyExistsException;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.exception.OtherExceptions;
import com.samia.gestion.clients.exception.UnauthorizedException;
import com.samia.gestion.clients.mapper.ProductMapper;
import com.samia.gestion.clients.repository.CareRepository;
import com.samia.gestion.clients.repository.CategoryRepository;
import com.samia.gestion.clients.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final CareRepository careRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository, CareRepository careRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
        this.careRepository = careRepository;
    }
    public Product mapToProduct(ProductDTO productDTO) {
        return productMapper.mapToProduct(productDTO);
    }

    public ProductDTO mapToProductDTO(Product product) {
        return ProductMapper.mapToProductDTO(product);
    }
    public ProductDTO createProduct(ProductDTO productDTO){
        if (productDTO.name() == null || productDTO.name().trim().isEmpty()){
            throw new OtherExceptions("Ce champ est obligatoire.");
        }
        if (productDTO.productPrice() < 0) {
            throw new OtherExceptions("Le prix du peoduit ne peut pas être négatif.");
        }

        Category category = null;
        if (productDTO.categoryDTO() != null && productDTO.categoryDTO().name() != null && !productDTO.categoryDTO().name().trim().isEmpty() && productDTO.categoryDTO().id() != null) {
            category = categoryRepository.findById(productDTO.categoryDTO().id())
                    .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));
        }

        if (productRepository.findByName(productDTO.name()).isPresent()) {
            throw new AlreadyExistsException("Ce produit existe déjà.");
        }

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new UnauthorizedException("Utilisateur non authentifié");
        }
        User userContext = (User) authentication.getPrincipal();
        Long userContextId = userContext.getId();

        Product product = mapToProduct(productDTO);
        product.setUserId(userContextId);
        product.setCategory(category);
        productRepository.save(product);
        productRepository.save(product);
        return mapToProductDTO(product);
    }

    public ProductDTO getProductById(Long id){
        if(id == null){
            throw new OtherExceptions("Id du produit ne peut pas etre null.");
        }
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("ce produit n'existe pas."));
       return mapToProductDTO(product) ;
    }

    public ProductDTO getProductByName(String name){
        if (name.isEmpty() || name.trim().isEmpty()){
            throw new OtherExceptions("le nom du produit ne peut pas etre vide");
        }
        Product product = productRepository.findByName(name).orElseThrow(() -> new NotFoundException("ce produit n'existe pas."));
        return mapToProductDTO(product) ;
    }

    public ProductDTO updateProduct(ProductDTO productDetails, Long id){
        if(id == null){
            throw new OtherExceptions("Id du produit ne peut pas etre null.");
        }

        if(productDetails.name() == null || productDetails.name().trim().isEmpty()){
            throw  new OtherExceptions("Ce champ est obligatoire.");
        }

        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("ce produit n'existe pas."));
        if (!product.getName().equals(productDetails.name())){
            if (productRepository.findByName(productDetails.name()).isPresent()){
                throw new AlreadyExistsException("ce produit existe déja.");
            }
        }
        product.setName(productDetails.name() !=null ? productDetails.name() : product.getName());
        product.setType(productDetails.type() !=null ? productDetails.type() : product.getType());
        product.setRefProduct(productDetails.refProduct() !=null ? productDetails.refProduct() : product.getRefProduct());
        product.setDescription(productDetails.description() !=null ? productDetails.description() : product.getDescription());
        product.setProductPrice(productDetails.productPrice());
        if(productDetails.categoryDTO() != null && productDetails.categoryDTO().name() != null){
            Category category = categoryRepository.findById(productDetails.categoryDTO().id())
                    .orElseThrow(() -> new NotFoundException("Catégorie introuvable"));
            product.setCategory(category);
        }
        Product updatedProduct = productRepository.save(product);
        return mapToProductDTO(updatedProduct);
    }

    public PaginationResponse<ProductDTO> getAllProducts(int page, int size, String search){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productRepository.searchProduct(search, pageRequest);
        List<ProductDTO> productDTOs = productPage.getContent().stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
        return new PaginationResponse<>(
                productDTOs,
                page,
                 productPage.getTotalPages(),
                 productPage.getTotalElements()
                );
    }

    @Transactional
    public void deleteProduct(Long id){
        if(id == null){
            throw new OtherExceptions("Id du produit ne peut pas etre null.");
        }
        careRepository.deleteAllByProductId(id);
        productRepository.deleteById(id);
    }

}
