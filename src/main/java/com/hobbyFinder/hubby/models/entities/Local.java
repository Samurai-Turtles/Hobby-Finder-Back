package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
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

    public Local(String street, String district, String number, String city, String state) {
        this.street = street;
        this.district = district;
        this.number = number;
        this.city = city;
        this.state = state;
        this.latitude = 0;
        this.longitude = 0;
    }

}
