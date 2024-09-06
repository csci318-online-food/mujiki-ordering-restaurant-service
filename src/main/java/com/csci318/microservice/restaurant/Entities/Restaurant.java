package com.csci318.microservice.restaurant.Entities;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.Entities.Events.RestaurantEvent;
import com.csci318.microservice.restaurant.Entities.ObjValue.PhoneNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant extends AbstractAggregateRoot<Restaurant> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String restaurantName;

    @Column(name = "description")
    private String description;

    @Column(name = "cuisine")
    @Enumerated(EnumType.STRING)
    private CuisineType cuisine;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Embedded
    private PhoneNumber restaurantPhone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "is_opened", columnDefinition = "boolean default false")
    private boolean isOpened;

    @Column(name = "create_at")
    private Timestamp createAt;

    @Column(name = "modify_at")
    private Timestamp modifyAt;

    @Column(name = "modify_by")
    private String modifyBy;

    @Column(name = "create_by")
    private String createBy;

    // Event registration handler
    public void registerRestaurant() {
        RestaurantEvent event = new RestaurantEvent();
        event.setEventName("register");
        event.setRestaurantId(this.getId());
        event.setRestaurantName(this.getRestaurantName());
        event.setDetails("Restaurant registered successfully.");
        event.setPhoneNumber(this.getRestaurantPhone()); // Set phone number
        registerEvent(event); // Method from AbstractAggregateRoot
    }

    public void updateDetails(String newName, String newAddress) {
        this.restaurantName = newName;
        RestaurantEvent event = new RestaurantEvent();
        event.setEventName("update_details");
        event.setRestaurantId(this.getId());
        event.setRestaurantName(this.getRestaurantName());
        event.setDetails("Details updated to: Name - " + newName + ", Address - " + newAddress);
        event.setPhoneNumber(this.getRestaurantPhone()); // Set phone number
        registerEvent(event);  // Method from AbstractAggregateRoot
    }
}
