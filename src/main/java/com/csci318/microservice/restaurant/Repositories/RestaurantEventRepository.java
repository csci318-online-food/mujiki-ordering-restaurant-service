package com.csci318.microservice.restaurant.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csci318.microservice.restaurant.Domain.Events.RestaurantEvent;

import java.util.List;
import java.util.UUID;

public interface RestaurantEventRepository extends JpaRepository<RestaurantEvent, UUID> {

}
