package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Table(name = "photos")
@Entity(name = "photos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String extension = StringUtils.EMPTY;

    private boolean isSaved = false;
}
