package com.csci318.microservice.restaurant.Mappers.Impl;

import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Restaurant;
import com.csci318.microservice.restaurant.Mappers.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantMapperImpl implements Mapper<Restaurant, RestaurantDTOResponse, RestaurantDTORequest> {

    @Override
    public RestaurantDTOResponse toDtos(Restaurant entity) {
        RestaurantDTOResponse dto = new RestaurantDTOResponse();
        dto.setId(entity.getId());
        dto.setRestaurantName(entity.getRestaurantName());
        dto.setRestaurantPhone(entity.getRestaurantPhone());
        dto.setEmail(entity.getEmail());
        dto.setCuisine(entity.getCuisine());
        dto.setOpenTime(entity.getOpenTime());
        dto.setCloseTime(entity.getCloseTime());
        dto.setRating(entity.getRating());
        dto.setDescription(entity.getDescription());
        dto.setOpened(entity.isOpened());
        dto.setRole(entity.getRole());
        return dto;
    }

    @Override
    public Restaurant toEntities(RestaurantDTORequest dto) {
        Restaurant entity = new Restaurant();
        entity.setRestaurantName(dto.getName());
        entity.setRestaurantPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setCuisine(dto.getCuisine());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        entity.setRating(dto.getRating());
        entity.setDescription(dto.getDescription());
        entity.setOpened(dto.isOpened());
        entity.setRole(dto.getRole());
        return entity;
    }

    @Override
    public List<RestaurantDTOResponse> toDtos(List<Restaurant> restaurants) {
        List<RestaurantDTOResponse> dtos = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            dtos.add(toDtos(restaurant));
        }
        return dtos;
    }

    @Override
    public List<Restaurant> toEntities(List<RestaurantDTORequest> dtos) {
        List<Restaurant> restaurants = new ArrayList<>();
        for (RestaurantDTORequest dto : dtos) {
            restaurants.add(toEntities(dto));
        }
        return restaurants;
    }

    public Restaurant toFilteredEntity(RestaurantDTOFilterRequest dto) {
        Restaurant entity = new Restaurant();
        entity.setRestaurantName(dto.getName());
        entity.setRestaurantPhone(dto.getPhone());
        entity.setCuisine(dto.getCuisine());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());
        entity.setRating(dto.getMinRating()); // Assuming minRating is used for setting rating
        entity.setOpened(dto.isOpened());
        return entity;
    }
}