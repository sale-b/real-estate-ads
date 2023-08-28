package com.fon.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RealEstateDetailsDto {
    private Long id;
    private Long userId;
    private String tittle;
    private String phone;
    private Double price;
    private String adType;
    private String realEstateType;
    private String roomsNumber;
    private String floor;
    private String description;
    private String location;
    private String city;
    private String cityRegion;
    private String citySubregion ;
    private Float livingSpaceArea;
    private String furnitureType;
    private String heatingType;
    private String imageUrl;
    private List<String> images;
    private GeoLocationDto geoLocationCoordinates;
    private Integer geoLocationZoom;
    private LocalDateTime createdOn;
}