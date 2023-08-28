package com.fon.entity.converter;

import com.fon.entity.enumeration.HeatingType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class HeatingTypeSetConverter implements AttributeConverter<Set<HeatingType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<HeatingType> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(HeatingType::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<HeatingType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(HeatingType::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
