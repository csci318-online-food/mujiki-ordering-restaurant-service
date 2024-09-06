package com.csci318.microservice.restaurant.Entities.Events;

import com.csci318.microservice.restaurant.Entities.ObjValue.PhoneNumber;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurant_event")
public class RestaurantEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "details")
    private String details;

    @Embedded
    private PhoneNumber phoneNumber;

    public RestaurantEvent() {}

    @Override
    public String toString() {
        return "RestaurantEvent{" +
                "eventName='" + eventName + '\'' +
                ", restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", details='" + details + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
