package com.fon.entity.converter;

import com.fon.entity.enumeration.RealEstateType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class RealEstateTypeSetConverter implements AttributeConverter<Set<RealEstateType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<RealEstateType> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(RealEstateType::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public Set<RealEstateType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        return Arrays.stream(dbData.split(DELIMITER))
                .map(RealEstateType::valueOf)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
