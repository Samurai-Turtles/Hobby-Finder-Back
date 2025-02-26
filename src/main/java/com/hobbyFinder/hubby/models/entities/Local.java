package com.hobbyFinder.hubby.models.entities;

public record Local(
        double latitude,
        double longitude,
        String street,
        String district,
        String number,
        String city,
        String state) {
}
