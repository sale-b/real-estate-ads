package com.fon.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AdType {
    SALE("Prodaja"),
    RENTAL("Izdavanje");

    private final String label;

}
