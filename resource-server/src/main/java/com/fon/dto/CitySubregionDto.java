package com.fon.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class CitySubregionDto extends BaseLocation {
    private GeoLocationDto geoLocation;
    private Integer zoom;
}
