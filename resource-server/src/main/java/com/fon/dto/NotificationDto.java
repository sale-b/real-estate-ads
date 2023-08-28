package com.fon.dto;

import lombok.Data;

@Data
public class NotificationDto {

    private Long id;
    private RealEstateDetailsDto realEstate;
    private Long filterId;
    private Boolean seen;

}
