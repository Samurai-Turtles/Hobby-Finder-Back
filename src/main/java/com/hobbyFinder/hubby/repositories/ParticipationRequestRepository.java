package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hobbyFinder.hubby.models.entities.Event;
import com.hobbyFinder.hubby.models.entities.ParticipationRequest;
import com.hobbyFinder.hubby.models.entities.User;
import java.util.List;


public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, UUID> {

    List<ParticipationRequest> findByUser(User user);

    Page<ParticipationRequest> findByEvent(Event event, Pageable pageable);

    Page<ParticipationRequest> findByUser(User user, Pageable pageable);

}
