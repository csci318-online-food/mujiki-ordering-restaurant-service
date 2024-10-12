package com.csci318.microservice.restaurant.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDTORequest {
    private String name;
    private String description;
    private double price;
    private boolean availability;
}
