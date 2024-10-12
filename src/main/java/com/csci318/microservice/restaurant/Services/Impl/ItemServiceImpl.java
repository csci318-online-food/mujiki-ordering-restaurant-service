package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.DTOs.ItemDTORequest;
import com.csci318.microservice.restaurant.DTOs.ItemDTOResponse;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Domain.Entities.Item;
import com.csci318.microservice.restaurant.Mappers.Impl.ItemMapper;
import com.csci318.microservice.restaurant.Repositories.ItemRepository;
import com.csci318.microservice.restaurant.Services.ItemService;
import com.csci318.microservice.restaurant.Services.RestaurantService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final RestaurantService restaurantService;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper, RestaurantService restaurantService) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.restaurantService = restaurantService;
    }

    public ItemDTOResponse createItemForRestaurant(ItemDTORequest itemDTORequest, UUID restaurantId) {
        try {
            RestaurantDTOResponse restaurant = restaurantService.getRestaurantById(restaurantId);
            if (restaurant == null) {
                throw new RuntimeException("Invalid restaurant ID.");
            }

            Item item = new Item();
            item.setName(itemDTORequest.getName());
            item.setRestaurantId(restaurantId);
            item.setDescription(itemDTORequest.getDescription());
            item.setPrice(itemDTORequest.getPrice());
            item.setAvailability(itemDTORequest.isAvailability());
            this.itemRepository.save(item);
            log.info("Item created for restaurant with restaurantId: " + restaurantId);
            return this.itemMapper.toDtos(item);
        } catch (Exception e) {
            log.error("Error creating item for restaurant with restaurantId: " + restaurantId);
            log.error(e.toString());
            return null;
        }
    }

    @Override
    public ItemDTOResponse getItemById(UUID id) {
        Item item = this.itemRepository.findById(id).orElse(null);
        if (item == null) {
            log.error("Item not found with id: " + id);
            return null;
        }
        return this.itemMapper.toDtos(item);
    }

}
