package com.example.customuser2.domain.type.converter;

import com.example.customuser2.domain.type.UserGrade;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserGradeAttributeConverter implements AttributeConverter<UserGrade, String> {

    @Override
    public String convertToDatabaseColumn(UserGrade attribute) {
        return attribute.getName();
    }

    @Override
    public UserGrade convertToEntityAttribute(String dbData) {
        return UserGrade.get(dbData);
    }
}
