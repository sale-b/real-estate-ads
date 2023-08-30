package com.fon.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FurnitureType {
    UNFURNISHED("Nenamešten"),
    SEMI_FURNISHED("Polunamešten"),
    FURNISHED("Namešten");

    private final String label;
}
