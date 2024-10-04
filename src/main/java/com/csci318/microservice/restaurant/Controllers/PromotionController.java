package com.csci318.microservice.restaurant.Controllers;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;
import com.csci318.microservice.restaurant.Services.Impl.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/create")
    @ManagedOperation(description = "Create a new promotion")
    public ResponseEntity<PromotionDTOResponse> createPromotion(@RequestBody PromotionDTORequest promotionDTORequest) {
        PromotionDTOResponse responseDTO = this.promotionService.createPromotion(promotionDTORequest);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @ManagedOperation(description = "Get promotions by restaurant id")
    public ResponseEntity<List<PromotionDTOResponse>>  getPromotionsByRestaurantId(@PathVariable UUID restaurantId) {
        List<PromotionDTOResponse> responseDTO = this.promotionService.getPromotionsByRestaurantId(restaurantId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{promotionId}")
    @ManagedOperation(description = "Get promotion by promotion id")
    public ResponseEntity<PromotionDTOResponse> getPromotionById(@PathVariable UUID promotionId) {
        PromotionDTOResponse responseDTO = this.promotionService.findPromotionById(promotionId);
        return ResponseEntity.ok(responseDTO);
    }

}
