package com.csci318.microservice.restaurant.DTOs;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.Domain.ValueObjs.PhoneNumber;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantDTORequest {
    private String name;
    private PhoneNumber phone;
    private String email;
    private String password;
    private CuisineType cuisine;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double rating;
    private String description;
    private boolean isOpened;
    private Roles role;
}
