package com.csci318.microservice.restaurant.Services;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;

import java.util.List;
import java.util.UUID;

public interface PromotionService {
    public PromotionDTOResponse createPromotion(PromotionDTORequest promotion);
    public List<PromotionDTOResponse> getPromotionsByRestaurantId(UUID restaurantId);
    public PromotionDTOResponse findPromotionById(UUID promotionId);
    public PromotionDTOResponse applyPromotion(UUID promotionId);
}
