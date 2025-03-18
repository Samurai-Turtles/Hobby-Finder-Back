package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository<Participation, UUID> {

    Optional<Participation> findByIdParticipation(UUID participationId);
}
