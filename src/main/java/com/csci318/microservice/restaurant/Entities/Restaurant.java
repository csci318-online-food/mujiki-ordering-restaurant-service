package com.csci318.microservice.restaurant.Entities;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.Constants.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "restaurant")
public class Restaurant {

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

    @Column(name = "phone")
    private String restaurantPhone;

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
}