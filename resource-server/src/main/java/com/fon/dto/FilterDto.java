package com.fon.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class FilterDto {
    private Long id;
    private Long userId;
    private CityDto location;
    private List<CitySubregionDto> microLocation;
    private Integer priceLess;
    private Integer priceHigher;
    private Float spaceAreaLess;
    private Float spaceAreaHigher;
    private String roomsNumberLess;
    private String roomsNumberHigher;
    private Set<String> type;
    private Set<String> adType;
    private Set<String> heatingType;
    private Set<String> floor;
    private Set<String> furniture;
    private Boolean hasPictures;
//    private Set<Float> coordinates;
    private String title;
    private Boolean subscribed;
    private Long newNotificationsCount;

}