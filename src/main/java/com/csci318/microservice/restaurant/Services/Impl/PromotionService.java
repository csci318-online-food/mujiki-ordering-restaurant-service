package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;
import com.csci318.microservice.restaurant.Domain.Entities.Promotion;
import com.csci318.microservice.restaurant.Mappers.Impl.PromotionMapper;
import com.csci318.microservice.restaurant.Repositories.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    /**
     * Restaurant Site
     */
    @Transactional
    public PromotionDTOResponse createPromotion(PromotionDTORequest promotion) {
        try {
            Promotion promotionEntity = new Promotion();
            promotionEntity.setId(UUID.randomUUID());
            promotionEntity.setRestaurantId(promotion.getRestaurantId());
            promotionEntity.setCode(promotion.getCode());
            promotionEntity.setDescription((promotion.getDescription()));
            promotionEntity.setExpiryDate((promotion.getExpiryDate()));
            promotionEntity.setPercentage(promotion.getPercentage());
            promotionEntity.setStock(promotion.getStock());
            this.promotionRepository.save(promotionEntity);
            PromotionDTOResponse promotionDTOResponse = this.promotionMapper.toDtos(promotionEntity);
            return promotionDTOResponse;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create promotion");
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

    // Make the promotion expired
    @Scheduled(fixedRate = 3000) // Check per 3 seconds (maybe)
    public void makePromotionExpired() {
        List<Promotion> promotions = this.promotionRepository.findAll();
        for (Promotion promotion : promotions) {
            if (promotion.getExpiryDate().before(new Timestamp(System.currentTimeMillis()))) {
                promotion.setActive(false);
                this.promotionRepository.save(promotion);
            }
        }
    }




}
