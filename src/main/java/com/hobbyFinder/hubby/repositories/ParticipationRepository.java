package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {

    Optional<Participation> findByIdParticipation(UUID participationId);

    @Query("SELECT p from Participation p WHERE p.idUser = :userId")
    Page<Participation> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT p from Participation p WHERE p.idEvent = :idEvent")
    Page<Participation> findByIdEvent(@Param("idEvent") UUID idEvent, Pageable pageable);
}
