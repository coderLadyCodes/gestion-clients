package com.samia.gestion.clients.repository;

import com.samia.gestion.clients.entity.Care;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareRepository extends JpaRepository<Care, Long> {
    List<Care> findByClientId(Long clientId);

    Optional<Care> findByProductId(Long id);

    void deleteAllByProductId(Long productId);

}
