package com.csci318.microservice.restaurant.Entities.ObjValue;

import jakarta.persistence.Embeddable;
import lombok.Value;

@Value
@Embeddable
public class PhoneNumber {
    private String countryCode;
    private String number;

    public PhoneNumber(String countryCode, String number) {
        this.countryCode = countryCode;
        this.number = number;
    }

    public PhoneNumber() {
        this.countryCode = "";
        this.number = "";
    }
}
