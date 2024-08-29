package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Restaurant;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ErrorTypes;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ServiceException;
import com.csci318.microservice.restaurant.Mappers.Impl.RestaurantMapperImpl;
import com.csci318.microservice.restaurant.Repositories.RestaurantRepository;
import com.csci318.microservice.restaurant.Services.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapperImpl restaurantMapper;
    private final ApplicationEventPublisher eventPublisher;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, RestaurantMapperImpl restaurantMapper, ApplicationEventPublisher eventPublisher) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = false)
    public RestaurantDTOResponse createRestaurant(RestaurantDTORequest restaurantDTORequest) {
        try {
            log.info("Creating restaurant with request: {}", restaurantDTORequest);
            Restaurant restaurant = this.restaurantMapper.toEntities(restaurantDTORequest);

            if (restaurantRepository.existsByEmail(restaurant.getEmail())) {
                log.error("Restaurant with email {} already exists", restaurant.getEmail());
                throw new ServiceException(ErrorTypes.RESTAURANT_ALREADY_EXIST.getMessage(), null, ErrorTypes.RESTAURANT_ALREADY_EXIST);
            }

            restaurant.setRole(Roles.RESTAURANT);
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(savedRestaurant);

            log.info("Restaurant created successfully with id: {}", savedRestaurant.getId());
            eventPublisher.publishEvent(responseDTO);
            return responseDTO;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;  // Re-throw service-level exceptions without additional processing
        } catch (Exception e) {
            log.error("Unexpected error occurred while creating restaurant: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantDTOResponse> filterRestaurants(RestaurantDTOFilterRequest filterReq) {
        try {
            log.info("Filtering restaurants with request: {}", filterReq);
            List<Restaurant> allRestaurants = restaurantRepository.findAll();
            log.info("Found {} restaurants in the database", allRestaurants.size());

            List<RestaurantDTOResponse> filteredRestaurants = new ArrayList<>();
            for (Restaurant restaurant : allRestaurants) {
                if (restaurantMatchesFilters(restaurant, filterReq)) {
                    RestaurantDTOResponse responseDTO = this.restaurantMapper.toDtos(restaurant);
                    filteredRestaurants.add(responseDTO);
                }
            }

            log.info("Found {} restaurants matching the filter criteria", filteredRestaurants.size());
            return filteredRestaurants;

        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while filtering restaurants: {}", e.getMessage(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }

    private boolean restaurantMatchesFilters(Restaurant restaurant, RestaurantDTOFilterRequest filterRequest) {
        try {
            return (filterRequest.getName() == null || restaurant.getRestaurantName().toLowerCase().contains(filterRequest.getName().toLowerCase()))
                    && (filterRequest.getCuisine() == null || restaurant.getCuisine().equals(filterRequest.getCuisine()))
                    && (filterRequest.getMinRating() == null || restaurant.getRating() >= filterRequest.getMinRating())
                    && (filterRequest.getMaxRating() == null || restaurant.getRating() <= filterRequest.getMaxRating())
                    && (!filterRequest.isOpened() || restaurant.isOpened());
        } catch (Exception e) {
            log.error("Error occurred while applying filters to restaurant {}: ", restaurant.getId(), e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }
}
