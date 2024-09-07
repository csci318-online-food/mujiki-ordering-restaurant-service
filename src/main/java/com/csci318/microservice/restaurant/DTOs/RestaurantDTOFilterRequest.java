package com.csci318.microservice.restaurant.DTOs;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.Entities.ObjValue.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDTOFilterRequest {
    private String name;
    private PhoneNumber phone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private CuisineType cuisine;
    private Double minRating;
    private Double maxRating;
    private boolean isOpened;
    private String postcode; // From address table (NOTE)
}
