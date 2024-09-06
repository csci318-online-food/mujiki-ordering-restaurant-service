package com.csci318.microservice.restaurant.DTOs;

public class RestaurantCreatedEvent {
    private final RestaurantDTOResponse restaurant;

    public RestaurantCreatedEvent(RestaurantDTOResponse restaurant) {
        this.restaurant = restaurant;
    }
    public RestaurantDTOResponse getRestaurant() {
        return restaurant;
    }
}