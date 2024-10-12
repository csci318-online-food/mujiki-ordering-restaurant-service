package com.csci318.microservice.restaurant.Services.Impl;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.csci318.microservice.restaurant.Domain.Relations.FeedbackEvent;
import com.csci318.microservice.restaurant.Services.RestaurantService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventHandler {
    private final RestaurantService restaurantService;

    public EventHandler(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Bean
    public Consumer<FeedbackEvent> handleFeedbackEvent() {
        return inputEvent -> {
            handleFeedbackEvent(inputEvent);
        };
    }

    public void handleFeedbackEvent(FeedbackEvent event) {
        log.info(
            "Received feedback event for restaurant " +
            event.getRestaurantId().toString() +
            ", average rating: " + Double.toString(event.getAverageRating())
        );

        restaurantService.updateRating(event.getRestaurantId(), event);
    }
}
