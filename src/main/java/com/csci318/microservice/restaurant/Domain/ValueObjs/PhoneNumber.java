package com.csci318.microservice.restaurant.Domain.ValueObjs;

import jakarta.persistence.Embeddable;
import lombok.Value;

@Value
@Embeddable
public class PhoneNumber {
    private final String countryCode;
    private final String number;

    public PhoneNumber(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    public PhoneNumber() {
        this.countryCode = "";
        this.number = "";
    }
}
