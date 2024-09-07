package com.csci318.microservice.restaurant.Controllers;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOFilterRequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTORequest;
import com.csci318.microservice.restaurant.DTOs.RestaurantDTOResponse;
import com.csci318.microservice.restaurant.Entities.Relations.Address;
import com.csci318.microservice.restaurant.Entities.Restaurant;
import com.csci318.microservice.restaurant.Services.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.endpoint.base-url}/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDTOResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<RestaurantDTOResponse> getRestaurantById(@PathVariable UUID id) {
        RestaurantDTOResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/signup")
    @ManagedOperation(description = "Create a new restaurant")
    public ResponseEntity<RestaurantDTOResponse> createRestaurant(@RequestBody RestaurantDTORequest restaurantDTORequest) {
        RestaurantDTOResponse responseDTO = restaurantService.createRestaurant(restaurantDTORequest);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTOResponse> updateRestaurant(
            @PathVariable UUID id,
            @RequestBody RestaurantDTORequest restaurantDTORequest) {
        RestaurantDTOResponse updatedRestaurant = restaurantService.updateRestaurant(id, restaurantDTORequest);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @GetMapping("/search")
    @ManagedOperation(description = "Search for restaurants with filter options")
    public ResponseEntity<List<RestaurantDTOResponse>> filterRestaurants(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "cuisine", required = false) CuisineType cuisine,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "opened", required = false) Boolean opened,
            @RequestParam(value = "postcode", required = false) String postcode){

        RestaurantDTOFilterRequest filterReq = new RestaurantDTOFilterRequest();
        filterReq.setName(name);
        filterReq.setCuisine(cuisine);
        filterReq.setMinRating(minRating);
        filterReq.setMaxRating(maxRating);
        filterReq.setOpened(opened != null && opened);

        List<RestaurantDTOResponse> filteredRestaurants = restaurantService.filterRestaurants(filterReq);

        if (filteredRestaurants == null || filteredRestaurants.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("Message", "There is no restaurant that matches the filter")
                    .body(null);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("Message", "Return the filtered restaurants successfully")
                .body(filteredRestaurants);
    }
    
    @GetMapping("/{email}")
    public RestaurantDTOResponse findRestaurantByEmail(@PathVariable String email) {
        return restaurantService.getRestaurantByEmail(email);
    }

    @GetMapping("/address/{restaurantId}")
    public Address getAddressByRestaurant(@PathVariable UUID restaurantId) {
        return restaurantService.getAddressByRestaurant(restaurantId);
    }
}