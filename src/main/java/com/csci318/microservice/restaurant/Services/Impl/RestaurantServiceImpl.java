package com.csci318.microservice.restaurant.Services.Impl;

import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Relations.Address;
import com.csci318.microservice.restaurant.Entities.Restaurant;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ErrorTypes;
import com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler.ServiceException;
import com.csci318.microservice.restaurant.Mappers.Impl.RestaurantMapperImpl;
import com.csci318.microservice.restaurant.Repositories.RestaurantRepository;
import com.csci318.microservice.restaurant.Services.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapperImpl restaurantMapper;
    private final ApplicationEventPublisher eventPublisher;

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
            throw e;
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

    @Override
    public Address getAddressByRestaurant(UUID restaurantId) {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ServiceException(ErrorTypes.RESTAURANT_NOT_FOUND.getMessage(), null, ErrorTypes.RESTAURANT_NOT_FOUND));
                return restTemplate.getForObject("http://localhost:83/forRestaurant/" + restaurantId, Address.class);
        } catch (ServiceException e) {
            log.error("Service exception: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while retrieving restaurant by id: ", e);
            throw new ServiceException(ErrorTypes.UNEXPECTED_ERROR.getMessage(), e, ErrorTypes.UNEXPECTED_ERROR);
        }
    }
}
