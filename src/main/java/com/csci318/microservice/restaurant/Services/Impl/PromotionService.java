package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.DTOs.PromotionDTORequest;
import com.csci318.microservice.restaurant.DTOs.PromotionDTOResponse;
import com.csci318.microservice.restaurant.Domain.Entities.Promotion;
import com.csci318.microservice.restaurant.Mappers.Impl.PromotionMapper;
import com.csci318.microservice.restaurant.Repositories.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }





}
