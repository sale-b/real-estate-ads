package com.fon.entity.converter;

import com.fon.entity.enumeration.Floor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class FloorSetConverter implements AttributeConverter<Set<Floor>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<Floor> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(Floor::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<Floor> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(Floor::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
