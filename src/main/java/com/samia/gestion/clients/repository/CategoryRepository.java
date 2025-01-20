package com.samia.gestion.clients.repository;

import com.samia.gestion.clients.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
     Optional<Category> findByName(String categoryName);
}
