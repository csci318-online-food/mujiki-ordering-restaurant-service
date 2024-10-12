package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Domain.Entities.Promotion;
import com.csci318.microservice.restaurant.Domain.Services.PromotionTracker;
import com.csci318.microservice.restaurant.Mappers.Impl.PromotionMapper;
import com.csci318.microservice.restaurant.Repositories.PromotionRepository;
import com.csci318.microservice.restaurant.Services.PromotionService;
import com.csci318.microservice.restaurant.Services.RestaurantService;

import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final RestaurantService restaurantService;

    private final PromotionTracker promotionTracker;

    public PromotionServiceImpl(PromotionRepository promotionRepository, PromotionMapper promotionMapper, RestaurantService restaurantService) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
        this.restaurantService = restaurantService;

        this.promotionTracker = new PromotionTracker();
    }

    /**
     * Restaurant Site
     */
    @Transactional
    public PromotionDTOResponse createPromotion(PromotionDTORequest promotion) {
        try {
            RestaurantDTOResponse restaurant = restaurantService.getRestaurantById(
                promotion.getRestaurantId()
            );
            if (restaurant == null) {
                throw new RuntimeException("Invalid restaurant ID.");
            }

            Promotion promotionEntity = new Promotion();
            promotionEntity.setId(UUID.randomUUID());
            promotionEntity.setRestaurantId(promotion.getRestaurantId());
            promotionEntity.setCode(promotion.getCode());
            promotionEntity.setDescription((promotion.getDescription()));
            promotionEntity.setExpiryDate((promotion.getExpiryDate()));
            promotionEntity.setPercentage(promotion.getPercentage());
            promotionEntity.setStock(promotion.getStock());
            promotionEntity.setActive(promotion.isActive());

            // Check for activity again with our tracker.
            promotionTracker.updatePromotionActive(promotionEntity);

            this.promotionRepository.save(promotionEntity);
            PromotionDTOResponse promotionDTOResponse = this.promotionMapper.toDtos(promotionEntity);
            return promotionDTOResponse;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create promotion", e);
        }
    }

    @Transactional
    public List<PromotionDTOResponse> getPromotionsByRestaurantId(UUID restaurantId) {
        try {
            List<Promotion> promotion = this.promotionRepository.findByRestaurantId(restaurantId);
            List<PromotionDTOResponse> promotionDTOResponse = this.promotionMapper.toDtos(promotion);
            return promotionDTOResponse;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get promotions");
        }
    }

    @Transactional
    public PromotionDTOResponse findPromotionById(UUID promotionId) {
        try {
            Promotion promotion = this.promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));
            PromotionDTOResponse promotionDTOResponse = this.promotionMapper.toDtos(promotion);
            return promotionDTOResponse;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find promotion");
        }
    }

    @Transactional
    public PromotionDTOResponse applyPromotion(UUID promotionId) {
        try {
            Promotion promotion = this.promotionRepository.findById(promotionId)
                    .orElseThrow(() -> new RuntimeException("Promotion not found"));

            promotionTracker.updatePromotionActive(promotion);
            boolean promotionActive = promotion.isActive();
            if (promotionActive) {
                promotion.decrementStock();
            }
            this.promotionRepository.save(promotion);

            if (promotionActive) {
                PromotionDTOResponse promotionDTOResponse = this.promotionMapper.toDtos(promotion);
                return promotionDTOResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to find promotion");
        }
    }

    // Make the promotion expired
    @Scheduled(fixedRate = 3000) // Check per 3 seconds (maybe)
    public void makePromotionExpired() {
        List<Promotion> promotions = this.promotionRepository.findAll();
        for (Promotion promotion : promotions) {
            if (promotionTracker.updatePromotionActive(promotion)) {
                this.promotionRepository.save(promotion);
            }
        }
    }
}
