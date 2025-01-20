package com.samia.gestion.clients.repository;

import com.samia.gestion.clients.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Page<Client> findAll(Pageable pageable);

    @Query("SELECT c FROM Client c WHERE " +
            "(LOWER(c.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.zipCode) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "OR :search IS NULL OR :search = ''")
    Page<Client> searchClient(@Param("search")String search, Pageable pageable);
}
