package com.samia.gestion.clients.repository;

import com.samia.gestion.clients.entity.Program;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("SELECT p FROM Program p LEFT JOIN FETCH p.cares WHERE p.id = :id")
    Optional<Program> findByIdWithCares(@Param("id") Long id);

    Optional<Program> findFirstByClientId(Long clientId);

    @Query("SELECT DISTINCT p FROM Program p LEFT JOIN FETCH p.cares WHERE p.clientId = :clientId")
    List<Program> findAllByClientId(@Param("clientId") Long clientId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Program p WHERE p.clientId = :clientId")
    void deleteAllByClientId(@Param("clientId") Long clientId);

}
