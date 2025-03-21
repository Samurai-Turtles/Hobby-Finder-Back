package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Local {

    @Column(nullable = false)
    private String street;
    
    @Column(nullable = false)
    private String district;
    
    @Column(nullable = false)
    private String number;
    
    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    private double latitude;
    private double longitude;

}
