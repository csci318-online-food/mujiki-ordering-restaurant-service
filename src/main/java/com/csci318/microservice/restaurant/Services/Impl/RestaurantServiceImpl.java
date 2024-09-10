// src/main/java/com/csci318/microservice/restaurant/Services/Impl/RestaurantServiceImpl.java

package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Events.RestaurantEvent;
import com.csci318.microservice.restaurant.Entities.Relations.Address;
import com.csci318.microservice.restaurant.Entities.Restaurant;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ErrorTypes;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ServiceException;
import com.csci318.microservice.restaurant.Mappers.Impl.RestaurantMapperImpl;
import com.csci318.microservice.restaurant.Repositories.RestaurantRepository;
import com.csci318.microservice.restaurant.Services.RestaurantService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapperImpl restaurantMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${address.url.service}")
    private String addressUrl;

    public RestaurantServiceImpl(RestTemplate restTemplate, RestaurantRepository restaurantRepository, RestaurantMapperImpl restaurantMapper, ApplicationEventPublisher eventPublisher) {
        this.restTemplate = restTemplate;
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDTOResponse> getAllRestaurants() {
        try {
            log.info("Retrieving all restaurants");
            List<Restaurant> allRestaurants = restaurantRepository.findAll();
            log.info("Found {} restaurants in the database", allRestaurants.size());

            List<RestaurantDTOResponse> responseDTOs = new ArrayList<>();
            for (Restaurant restaurant : allRestaurants) {
                RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(restaurant);
                responseDTOs.add(responseDTO);
            }

            return responseDTOs;

        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving all restaurants: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantDTOResponse getRestaurantById(UUID id) {
        try {
            log.info("Retrieving restaurant with id: {}", id);
            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));
            return restaurantMapper.toDtos(restaurant);
        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving restaurant by id: ", e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    @Override
    public RestaurantDTOResponse updateRating(UUID id, double rating) {
        try {
            log.info("Updating rating for restaurant with id: {} to {}", id, rating);
            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));

            restaurant.setRating(rating);
            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(updatedRestaurant);

            log.info("Rating updated successfully for restaurant with id: {}", updatedRestaurant.getId());
            publishRestaurantEvent(updatedRestaurant, "update_rating", "Rating updated to: " + rating);
            return responseDTO;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating rating for restaurant: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public RestaurantDTOResponse createRestaurant(RestaurantDTORequest restaurantDTORequest) {
        try {
            log.info("Creating restaurant with request: {}", restaurantDTORequest);
            Restaurant restaurant = this.restaurantMapper.toEntities(restaurantDTORequest);
            // TODO: Control UUID generation.
            restaurant.setId(UUID.randomUUID());

            if (restaurantRepository.existsByEmail(restaurant.getEmail())) {
                log.error("Restaurant with email {} already exists", restaurant.getEmail());
                throw new ServiceException(ErrorTypes.RESTAURANT_ALREADY_EXIST.getMessage(), null, ErrorTypes.RESTAURANT_ALREADY_EXIST);
            }

            restaurant.setRole(Roles.RESTAURANT);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(savedRestaurant);

            log.info("Restaurant created successfully with id: {}", savedRestaurant.getId());
            publishRestaurantEvent(savedRestaurant, "register", "Restaurant registered successfully.");
            return responseDTO;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating restaurant: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public RestaurantDTOResponse updateRestaurant(UUID id, RestaurantDTORequest restaurantDTORequest) {
        try {
            log.info("Updating restaurant with id: {} and request: {}", id, restaurantDTORequest);
            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));

            restaurant.setRestaurantName(restaurantDTORequest.getName());
            restaurant.setCuisine(restaurantDTORequest.getCuisine());
            restaurant.setOpened(restaurantDTORequest.isOpened());

            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(updatedRestaurant);

            log.info("Restaurant updated successfully with id: {}", updatedRestaurant.getId());
            publishRestaurantEvent(updatedRestaurant, "update_details", "Details updated to: Name - " + restaurantDTORequest.getName());
            return responseDTO;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while updating restaurant: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    public List<RestaurantDTOResponse> filterRestaurants(RestaurantDTOFilterRequest filterReq) {
        try {
            log.info("Filtering restaurants with request: {}", filterReq);
            List<Restaurant> allRestaurants = restaurantRepository.findAll();
            log.info("Found {} restaurants in the database", allRestaurants.size());

            // Check if filter request is empty
            if (isFilterRequestEmpty(filterReq)) {
                log.info("No filter applied, returning all restaurants");
                return allRestaurants.stream()
                        .map(restaurantMapper::toDtos)
                        .collect(Collectors.toList());
            }

            List<RestaurantDTOResponse> filteredRestaurants = new ArrayList<>();
            for (Restaurant restaurant : allRestaurants) {
                if (restaurantMatchesFilters(restaurant, filterReq)) {
                    RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(restaurant);
                    filteredRestaurants.add(responseDTO);
                }
            }

            if (filteredRestaurants.isEmpty()) {
                log.info("No restaurants found matching the filter criteria");
                // Return emtpy list
                return new ArrayList<>();
            } else {
                log.info("Found {} restaurants matching the filter criteria", filteredRestaurants.size());
            }

            return filteredRestaurants;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while filtering restaurants: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    private boolean isFilterRequestEmpty(RestaurantDTOFilterRequest filterReq) {
        return (filterReq.getName() == null || filterReq.getName().isEmpty()) &&
                (filterReq.getCuisine() == null) &&
                (filterReq.getMinRating() == null) &&
                (filterReq.getMaxRating() == null) &&
                (filterReq.getPostcode() == null || filterReq.getPostcode().isEmpty()) &&
                !filterReq.isOpened(); // Checks if isOpened is false
    }

    private boolean restaurantMatchesFilters(Restaurant restaurant, RestaurantDTOFilterRequest filterRequest) {
        try {
            // Fetch address from address service
            Address address = restTemplate.getForObject(addressUrl + "/forRestaurant/" + restaurant.getId(), Address.class);
            log.info("Fetched address for restaurant {}: {}", restaurant.getId(), address);

            // Apply filtering criteria
            if ((filterRequest.getName() != null && !restaurant.getRestaurantName().toLowerCase().contains(filterRequest.getName().toLowerCase()))
                    || (filterRequest.getCuisine() != null && !restaurant.getCuisine().equals(filterRequest.getCuisine()))
                    || (filterRequest.getMinRating() != null && restaurant.getRating() < filterRequest.getMinRating())
                    || (filterRequest.getMaxRating() != null && restaurant.getRating() > filterRequest.getMaxRating())
                    || (filterRequest.isOpened() && !restaurant.isOpened())) {
                return false;
            }

            // Check postcode filter
            if (filterRequest.getPostcode() != null && !filterRequest.getPostcode().isEmpty()) {
                if (address == null || !address.getPostcode().equals(filterRequest.getPostcode())) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            log.error("Error occurred while applying filters to restaurant {}: ", restaurant.getId(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }



    @Override
    @Transactional
    public RestaurantDTOResponse getRestaurantByEmail(String email) {
        try {
            Restaurant restaurant = restaurantRepository.findByEmail(email)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));
            return restaurantMapper.toDtos(restaurant);
        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving restaurant by email: ", e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    // Linking services
    @Override
    public Address getAddressByRestaurant(UUID restaurantId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));
                return restTemplate.getForObject(addressUrl + "/" + restaurant.getId(), Address.class);
        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving restaurant by id: ", e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    // Event registration handler for restaurant
    private void publishRestaurantEvent(Restaurant restaurant, String eventName, String details) {
        RestaurantEvent event = new RestaurantEvent();
        event.setEventName(eventName);
        event.setRestaurantId(restaurant.getId());
        event.setRestaurantName(restaurant.getRestaurantName());
        event.setPhoneNumber(restaurant.getRestaurantPhone());
        event.setDetails(details);
        eventPublisher.publishEvent(event);
    }
}
