package com.csci318.microservice.restaurant.Domain.Services;

import com.csci318.microservice.restaurant.Domain.Entities.Promotion;

import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PromotionTracker {
    // Checks whether this promotion is supposed to be active.
    public boolean shouldPromotionBeActive(Promotion promotion) {
        if (!promotion.isActive()) {
            return false;
        }

        if (promotion.getStock() == 0) {
            return false;
        }

        if (promotion.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    // Updates the promotion's active status.
    // Returns true if changes have been made to the object.
    public boolean updatePromotionActive(Promotion promotion) {
        boolean status = promotion.isActive();
        boolean newStatus = shouldPromotionBeActive(promotion);
        if (status != newStatus) {
            promotion.setActive(newStatus);
            return false;
        }
        return true;
    }
}
