package com.samia.gestion.clients.repository;

import com.samia.gestion.clients.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.category c " +
            "WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.refProduct) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.type) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProduct(@Param("search") String search, Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.category = NULL WHERE p.category.id = :categoryId")
    void setCategoryToNull(@Param("categoryId") Long categoryId);
}
