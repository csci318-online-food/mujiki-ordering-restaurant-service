package com.csci318.microservice.restaurant.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PromotionDTORequest {
    private UUID id;
    private UUID restaurantId;
    private String code;
    private String description;
    private int percentage;
    private Timestamp expiryDate;
    private int stock;
}
