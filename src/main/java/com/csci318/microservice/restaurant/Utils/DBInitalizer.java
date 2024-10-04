package com.csci318.microservice.restaurant.Utils;

import com.csci318.microservice.restaurant.Constants.CuisineType;
import com.csci318.microservice.restaurant.Constants.Roles;
import com.csci318.microservice.restaurant.Domain.Entities.Restaurant;
import com.csci318.microservice.restaurant.Domain.ValueObjs.PhoneNumber;
import com.csci318.microservice.restaurant.Repositories.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class DBInitalizer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    public DBInitalizer(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (restaurantRepository.count() == 0) {
            // Add default restaurants
            Restaurant restaurant1 = new Restaurant();
            restaurant1.setId(UUID.randomUUID());
            restaurant1.setRestaurantName("Chat Thai");
            restaurant1.setDescription("Authentic Thai cuisine");
            restaurant1.setCuisine(CuisineType.THAI);
            restaurant1.setOpenTime(LocalTime.of(10, 0));
            restaurant1.setCloseTime(LocalTime.of(22, 0));
            restaurant1.setRestaurantPhone(new PhoneNumber("+61", "1112223333"));
            restaurant1.setEmail("chat.thai@example.com");
            restaurant1.setPassword("password1");
            restaurant1.setRating(4.5);
            restaurant1.setRole(Roles.RESTAURANT);
            restaurant1.setOpened(true);
            restaurant1.setCreateAt(new Timestamp(System.currentTimeMillis()));
            restaurant1.setModifyAt(new Timestamp(System.currentTimeMillis()));
            restaurant1.setCreateBy("system");
            restaurant1.setModifyBy("system");

            Restaurant restaurant2 = new Restaurant();
            restaurant2.setId(UUID.randomUUID());
            restaurant2.setRestaurantName("Sakura Sushi");
            restaurant2.setDescription("Fresh and delicious sushi");
            restaurant2.setCuisine(CuisineType.JAPANESE);
            restaurant2.setOpenTime(LocalTime.of(11, 0));
            restaurant2.setCloseTime(LocalTime.of(23, 0));
            restaurant2.setRestaurantPhone(new PhoneNumber("+81", "4445556666"));
            restaurant2.setEmail("sakura.sushi@example.com");
            restaurant2.setPassword("password2");
            restaurant2.setRating(4.8);
            restaurant2.setRole(Roles.RESTAURANT);
            restaurant2.setOpened(true);
            restaurant2.setCreateAt(new Timestamp(System.currentTimeMillis()));
            restaurant2.setModifyAt(new Timestamp(System.currentTimeMillis()));
            restaurant2.setCreateBy("system");
            restaurant2.setModifyBy("system");

            restaurantRepository.save(restaurant1);
            restaurantRepository.save(restaurant2);
        }


    }
}
