package com.fon.entity.converter;

import com.fon.entity.CitySubregion;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class MicroLocationSetConverter implements AttributeConverter<List<CitySubregion>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<CitySubregion> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(CitySubregion::getId)
                .map(Object::toString)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<CitySubregion> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(id -> CitySubregion.builder().id(Long.parseLong(id)).build())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
