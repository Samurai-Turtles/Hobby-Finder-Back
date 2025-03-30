package com.hobbyFinder.hubby.controllerTest.EventTests;

import lombok.Data;

import java.util.List;
@Data
public class CustomPage<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    // getters e setters
}
