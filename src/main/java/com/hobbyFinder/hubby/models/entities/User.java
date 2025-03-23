package com.hobbyFinder.hubby.models.entities;

import com.hobbyFinder.hubby.models.enums.InterestEnum;
import com.hobbyFinder.hubby.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<ParticipationRequest> requests = new ArrayList<>();

    private String email;
    private String username;
    private String password;
    private UserRole role;
    private String fullName;
    private String bio;
    @ElementCollection
    private List<InterestEnum> interests;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Participation> participations;

    @Transient
    private double stars;


    public User(String email, String username, String password, String fullName) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = UserRole.USER;
        this.interests = new ArrayList<>();
        this.participations = new ArrayList<>();
        this.requests = new ArrayList<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}