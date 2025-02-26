package com.hobbyFinder.hubby.models.dto;

public record LocalDto(
        double latitude,
        double longitude,
        String street,
        String district,
        String number,
        String city,
        String state) {
}
