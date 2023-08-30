package com.fon.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HeatingType {
    NO_HEATING("Bez grejanja"),
    CENTRAL_HEATING("Centralno grejanje"),
    ELECTRIC_HEATING("Električno grejanje"),
    GAS_HEATING("Gasno grejanje"),
    RADIATOR_HEATING("Grejanje preko radijatora"),
    HEAT_PUMP("Toplotne pumpe"),
    TILE_STOVE("Kaljeva peć"),
    NORWEGIAN_RADIATORS("Norveški radijatori"),
    MARBLE_RADIATORS("Mermerni radijatori"),
    UNDERFLOOR_HEATING("Podno grejanje");

    private final String label;
}
