package com.csci318.microservice.restaurant.Exceptions.ServiceExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorTypes {

    // General Errors for Service Layer
    UNEXPECTED_ERROR("UNEXPECTED_ERROR", "An unexpected error occurred!"),

    // RESTAURANT_ERROR
     RESTAURANT_NOT_FOUND("RESTAURANT_NOT_FOUND", "Can not find the restaurant!"),
     RESTAURANT_ALREADY_EXIST("RESTAURANT_ALREADY_EXIST", "Restaurant already exist!");

    private final String code;
    private final String message;


}
