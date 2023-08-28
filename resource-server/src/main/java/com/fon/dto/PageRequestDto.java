package com.fon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {

    private FilterDto filters;
    private Integer page;
    private Integer size;
}
