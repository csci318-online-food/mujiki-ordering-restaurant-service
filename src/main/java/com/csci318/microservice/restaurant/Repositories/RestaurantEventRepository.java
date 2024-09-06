package com.csci318.microservice.restaurant.Repositories;

import com.csci318.microservice.restaurant.Entities.Events.RestaurantEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RestaurantEventRepository extends JpaRepository<RestaurantEvent, UUID> {

}
