package com.csci318.microservice.restaurant.Listeners;

import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Domain.Events.RestaurantEvent;
import com.csci318.microservice.restaurant.Repositories.RestaurantEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEventListener {

    private static final Logger log = LoggerFactory.getLogger(RestaurantEventListener.class);
    private final RestaurantEventRepository restaurantEventRepository;

    public RestaurantEventListener(RestaurantEventRepository restaurantEventRepository) {
        this.restaurantEventRepository = restaurantEventRepository;
    }

    @EventListener
    public void handleRestaurantCreatedEvent(RestaurantEvent event) {
        restaurantEventRepository.save(event);
        log.info("Restaurant created: {}", event);
    }

    @EventListener
    public void handleRestaurantUpdatedEvent(RestaurantEvent event) {
        restaurantEventRepository.save(event);
        log.info("Restaurant updated: {}", event);
    }
}
