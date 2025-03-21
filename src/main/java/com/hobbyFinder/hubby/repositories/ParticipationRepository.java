package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.Avaliation;
import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {

    Optional<Participation> findByIdParticipation(UUID participationId);


    @Query("SELECT AVG(a.stars) FROM Participation p JOIN p.avaliation a where p.idEvent = :eventId")
    double avgStarsByEvent(UUID eventId);

    @Query("SELECT a FROM Avaliation a WHERE a.participation.idEvent = :eventId AND a.comment IS NOT NULL AND a.comment <> '' ORDER BY a.stars DESC")
    List<Avaliation> getAvaliationByEventOrdered(UUID eventId);

    @Query("SELECT AVG(a.stars) FROM Avaliation a " +
            "JOIN a.participation p " +
            "WHERE p.idUser = :userId " +
            "AND p.position = com.hobbyFinder.hubby.models.enums.ParticipationPosition.CREATOR " +
            "AND a.comment IS NOT NULL")
    Double findAverageStarsByUser(UUID userId);
}
