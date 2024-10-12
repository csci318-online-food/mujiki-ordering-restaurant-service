package com.csci318.microservice.restaurant.Services;

import com.csci318.microservice.restaurant.DTOs.ItemDTORequest;
import com.csci318.microservice.restaurant.DTOs.ItemDTOResponse;

import java.util.UUID;

public interface ItemService {

    ItemDTOResponse createItemForRestaurant(ItemDTORequest itemDTORequest, UUID restaurantId);

    ItemDTOResponse getItemById(UUID id);
}
