package com.example.customuser2.domain.type.converter;

import com.example.customuser2.domain.type.UserGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserGradeAttributeConverterTest {

    private UserGradeAttributeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UserGradeAttributeConverter();
    }

    @Test
    void convertToDatabaseColumn() {

        // when
        var dbColumn = converter.convertToDatabaseColumn(UserGrade.GOLD);

        // then
        assertThat(dbColumn).isEqualTo("gold");
    }

    @Test
    void convertToEntityAttribute() {

        // when
        var grade = converter.convertToEntityAttribute("gold");

        // then
        assertThat(grade).isEqualTo(UserGrade.GOLD);
    }
}