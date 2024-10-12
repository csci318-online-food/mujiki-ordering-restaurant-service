package com.csci318.microservice.restaurant.Controllers;

import com.csci318.microservice.restaurant.DTOs.ItemDTORequest;
import com.csci318.microservice.restaurant.DTOs.ItemDTOResponse;
import com.csci318.microservice.restaurant.Services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${api.endpoint.base-url}/item")
public class ItemController {

    private final ItemService itemService;


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTOResponse> getItemById(@PathVariable UUID id) {
        ItemDTOResponse itemDTOResponse = this.itemService.getItemById(id);
        if (itemDTOResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemDTOResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<ItemDTOResponse> createItemForRestaurant(@RequestBody ItemDTORequest itemDTORequest, @RequestParam(name = "restaurantId") UUID restaurantId) {
        try {
            ItemDTOResponse itemDTOResponse = this.itemService.createItemForRestaurant(itemDTORequest, restaurantId);
            if (itemDTOResponse == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(itemDTOResponse);
        } catch (Exception e) {
            return null;
        }
    }
}
