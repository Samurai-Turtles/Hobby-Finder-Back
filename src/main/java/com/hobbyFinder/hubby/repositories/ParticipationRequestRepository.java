package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hobbyFinder.hubby.models.entities.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, UUID> {

}
