package com.csci318.microservice.restaurant.Services;

import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantDTOResponse createRestaurant(RestaurantDTORequest restaurantDTORequest);
    List<RestaurantDTOResponse> filterRestaurants(RestaurantDTOFilterRequest filterReq);
}
