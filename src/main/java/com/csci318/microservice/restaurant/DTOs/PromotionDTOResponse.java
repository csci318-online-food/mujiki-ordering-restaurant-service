package com.csci318.microservice.restaurant.DTOs;

import java.sql.Timestamp;
import java.util.UUID;

public class PromotionDTOResponse {
    private UUID id;
    private UUID restaurantId;
    private String code;
    private String description;
    private int percentage;
    private Timestamp expiryDate;
    private int stock;
}
