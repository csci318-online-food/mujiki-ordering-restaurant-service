package com.csci318.microservice.restaurant.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime expiryDate;
    private boolean isActive;
    private int stock;
}
