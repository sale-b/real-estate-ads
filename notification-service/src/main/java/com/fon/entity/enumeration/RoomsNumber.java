package com.fon.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoomsNumber {

    N_0_5("0.5"),
    N_1("1"),
    N_1_5("1.5"),
    N_2("2"),
    N_2_5("2.5"),
    N_3("3"),
    N_3_5("3.5"),
    N_4("4"),
    N_4_5("4.5"),
    N_5("5"),
    N_5_PLUS("5+");

    private final String label;
    
}
