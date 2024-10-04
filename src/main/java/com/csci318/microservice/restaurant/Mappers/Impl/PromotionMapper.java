package com.csci318.microservice.restaurant.Mappers.Impl;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;
import com.csci318.microservice.restaurant.Domain.Entities.Promotion;
import com.csci318.microservice.restaurant.Mappers.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PromotionMapper implements Mapper<Promotion, PromotionDTOResponse,PromotionDTORequest> {

    @Override
    public PromotionDTOResponse toDtos(Promotion entity) {
        PromotionDTOResponse promotionDto = new PromotionDTOResponse();
        promotionDto.setId(entity.getId());
        promotionDto.setRestaurantId(entity.getRestaurantId());
        promotionDto.setCode(entity.getCode());
        promotionDto.setDescription(entity.getDescription());
        promotionDto.setExpiryDate(entity.getExpiryDate());
        promotionDto.setActive(entity.isActive());
        promotionDto.setPercentage(entity.getPercentage());
        promotionDto.setStock(entity.getStock());
        return promotionDto;
    }

    @Override
    public Promotion toEntities(PromotionDTORequest dto) {
        return null;
    }


    @Override
    public List<PromotionDTOResponse> toDtos(List<Promotion> promotions) {
        List<PromotionDTOResponse> promotionDTORequests = new ArrayList<>();
        for (Promotion promotion : promotions) {
            promotionDTORequests.add(toDtos(promotion));
        }
        return promotionDTORequests;
    }

    @Override
    public List<Promotion> toEntities(List<PromotionDTORequest> dtos) {
        List<Promotion> promotions = new ArrayList<>();
        for (PromotionDTORequest promotionDTORequest : dtos) {
            promotions.add(toEntities(promotionDTORequest));
        }
        return promotions;
    }


}
