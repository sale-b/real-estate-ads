package com.fon.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RealEstateType {
    APARTMENT("Stan"),
    HOUSE("Kuća"),
    OFFICE_SPACE("Poslovni prostor");

    private final String label;
}
