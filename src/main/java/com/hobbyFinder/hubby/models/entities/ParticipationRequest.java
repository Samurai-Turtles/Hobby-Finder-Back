package com.hobbyFinder.hubby.models.entities;

import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class ParticipationRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

}
