package com.csci318.microservice.restaurant.Entities.ObjValue;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.getCountryCode() + "-" + phoneNumber.getNumber();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split("-");
        return new PhoneNumber(parts[0], parts[1]);
    }
}

