package com.csci318.microservice.restaurant.Repositories;

import com.csci318.microservice.restaurant.Domain.Entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<Promotion, UUID> {

    @Query("SELECT P FROM Promotion P WHERE P.restaurantId = :restaurantId")
    List<Promotion> findByRestaurantId(UUID restaurantId);
}
