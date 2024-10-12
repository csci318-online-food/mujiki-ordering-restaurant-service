package com.csci318.microservice.restaurant.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csci318.microservice.restaurant.Domain.Entities.Item;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

}
