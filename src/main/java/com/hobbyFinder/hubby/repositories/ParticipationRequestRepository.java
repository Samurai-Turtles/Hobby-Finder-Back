package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, UUID> {

    Page<ParticipationRequest> findByEvent(Event event, Pageable pageable);

}
