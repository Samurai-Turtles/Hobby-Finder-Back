package com.hobbyFinder.hubby.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hobbyFinder.hubby.models.entities.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

}
