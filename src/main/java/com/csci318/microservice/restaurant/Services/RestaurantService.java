package com.csci318.microservice.restaurant.Services;

import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Relations.Address;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {

    List<RestaurantDTOResponse> getAllRestaurants();
    RestaurantDTOResponse createRestaurant(RestaurantDTORequest restaurantDTORequest);
    RestaurantDTOResponse updateRestaurant(UUID id, RestaurantDTORequest restaurantDTORequest);
    List<RestaurantDTOResponse> filterRestaurants(RestaurantDTOFilterRequest filterReq);
    RestaurantDTOResponse getRestaurantByEmail(String email);
    Address getAddressByRestaurant(UUID restaurantId);
    RestaurantDTOResponse getRestaurantById(UUID id);
}
