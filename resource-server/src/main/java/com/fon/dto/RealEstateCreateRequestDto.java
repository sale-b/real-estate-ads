package com.fon.dto;

import com.fon.entity.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateCreateRequestDto {

    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String phone;
    @NotNull
    private Integer price;
    @NotNull
    private String adType;
    @NotNull
    private String realEstateType;
    @NotNull
    private String roomsNumber;
    @NotNull
    private String floor;
    @NotNull
    private String description;
    @NotNull
    private Long location;
    @NotNull
    private GeoLocation geoLocation;
    @NotNull
    private Float livingSpaceArea;
    @NotNull
    private String furnitureType;
    @NotNull
    private String heatingType;
    @NotNull
    private Boolean hasPictures;

}
