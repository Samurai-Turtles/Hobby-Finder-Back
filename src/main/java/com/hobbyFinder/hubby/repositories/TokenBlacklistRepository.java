package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<RevokedToken, Long> {
    boolean existsByToken(String token);
}
