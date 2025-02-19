package com.hobbyFinder.hubby.repositories;

import com.hobbyFinder.hubby.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByUsername(String username);
    UserDetails findByEmail(String email);
}
