package com.samia.gestion.clients.mapper;

import com.samia.gestion.clients.DTO.CategoryDTO;
import com.samia.gestion.clients.DTO.ProductDTO;
import com.samia.gestion.clients.entity.Category;
import com.samia.gestion.clients.entity.Product;
import com.samia.gestion.clients.exception.NotFoundException;
import com.samia.gestion.clients.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final CategoryRepository categoryRepository;

    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product mapToProduct(ProductDTO productDTO){
        Category category = null;
        if (productDTO.categoryDTO() != null && productDTO.categoryDTO().name() != null) {
            category = categoryRepository.findByName(productDTO.categoryDTO().name())
                    .orElseThrow(() -> new NotFoundException("Category not found."));
        }
        return new Product(
                productDTO.id(),
                productDTO.userId(),
                productDTO.type(),
                productDTO.name(),
                productDTO.refProduct(),
                productDTO.description(),
                productDTO.productPrice(),
                category
        );
    }
    public static ProductDTO mapToProductDTO(Product product){
        CategoryDTO categoryDTO = null;
        if (product.getCategory() != null) {
            categoryDTO = new CategoryDTO(
                    product.getCategory().getId(),
                    product.getCategory().getUserId(),
                    product.getCategory().getName()
            );
        }

        return new ProductDTO(
                product.getId(),
                product.getUserId(),
                product.getType(),
                product.getName(),
                product.getRefProduct(),
                product.getDescription(),
                categoryDTO,
                product.getProductPrice()

        );
    }
}
