package com.fon.entity.converter;

import com.fon.entity.enumeration.AdType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class AdTypeSetConverter implements AttributeConverter<Set<AdType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<AdType> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(AdType::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<AdType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(AdType::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
