package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.Evaluation;
import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {

    Optional<Participation> findByIdParticipation(UUID participationId);

    @Query("SELECT p from Participation p WHERE p.idUser = :userId")
    Page<Participation> findByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT p from Participation p WHERE p.idEvent = :idEvent")
    Page<Participation> findByIdEvent(@Param("idEvent") UUID idEvent, Pageable pageable);


    @Query("SELECT AVG(a.stars) FROM Participation p JOIN p.evaluation a where p.idEvent = :eventId")
    double avgStarsByEvent(UUID eventId);

    @Query("SELECT a FROM Evaluation a WHERE a.participation.idEvent = :eventId AND a.comment IS NOT NULL AND a.comment <> '' ORDER BY a.stars DESC")
    List<Evaluation> getAvaliationByEventOrdered(UUID eventId);

    @Query("SELECT AVG(a.stars) FROM Evaluation a " +
            "JOIN a.participation p " +
            "JOIN Event e ON p.idEvent = e.id " +
            "WHERE e.creator.id = :userId " +
            "AND a IS NOT NULL")
    Double findAverageStarsByUser(UUID userId);
}
