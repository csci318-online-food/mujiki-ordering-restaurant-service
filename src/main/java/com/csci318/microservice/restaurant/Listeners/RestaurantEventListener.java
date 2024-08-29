package com.csci318.microservice.restaurant.Listeners;

import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RestaurantEventListener {

    private static final Logger log = LoggerFactory.getLogger(RestaurantEventListener.class);

    @EventListener
    public void handleRestaurantCreatedEvent(RestaurantDTOResponse event) {
        log.info("Restaurant created: {}", event.getRestaurantName());
    }
}
