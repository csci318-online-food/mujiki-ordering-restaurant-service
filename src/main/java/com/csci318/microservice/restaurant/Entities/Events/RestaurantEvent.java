package com.csci318.microservice.restaurant.Entities.Events;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@Entity
public class RestaurantEvent {
    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String eventName;

    @Column
    private UUID restaurantId;

    @Column
    private String restaurantName;

    @Column
    private String details;

    public RestaurantEvent() {}

    // Getters and Setters

    @Override
    public String toString() {
        return "RestaurantEvent{" +
                "eventName='" + eventName + '\'' +
                ", restaurantId='" + restaurantId + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}

