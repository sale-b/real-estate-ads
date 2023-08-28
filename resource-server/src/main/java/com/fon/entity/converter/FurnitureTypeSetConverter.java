package com.fon.entity.converter;

import com.fon.entity.enumeration.FurnitureType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class FurnitureTypeSetConverter implements AttributeConverter<Set<FurnitureType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<FurnitureType> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(FurnitureType::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<FurnitureType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(FurnitureType::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
