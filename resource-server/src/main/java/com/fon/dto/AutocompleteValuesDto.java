package com.fon.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AutocompleteValuesDto {

    private List<String> adTypes;
    private List<String> realEstateTypes;
    private List<String> roomsNumbers;
    private List<String> floors;
    private List<String> heatingTypes;
    private List<String> furnitureTypes;
    private List<CityDto> cities;

}
