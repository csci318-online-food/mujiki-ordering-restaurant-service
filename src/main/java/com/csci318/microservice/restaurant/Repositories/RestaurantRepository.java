package com.csci318.microservice.restaurant.Repositories;

import com.csci318.microservice.restaurant.Entities.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Restaurant r WHERE r.email = :email")
    Boolean existsByEmail(String email);

    @Query("SELECT r FROM Restaurant r WHERE r.email = :email")
    Optional<Restaurant> findByEmail(String email);
}
