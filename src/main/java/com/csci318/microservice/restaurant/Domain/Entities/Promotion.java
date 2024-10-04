package com.csci318.microservice.restaurant.Domain.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion {

    @Id
    private UUID id;

    @Column(name = "restaurant_id")
    private UUID restaurantId;

    @Column(name = "discount_code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "percentage")
    private int percentage;

    @Column(name = "expiry_date")
    private Timestamp expiryDate;

    @Column(name = "stock")
    private int stock;

}
