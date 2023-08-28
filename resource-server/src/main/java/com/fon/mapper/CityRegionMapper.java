package com.fon.mapper;

import com.fon.dto.CityRegionDto;
import com.fon.entity.CityRegion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityRegionMapper {

    CityRegionMapper INSTANCE = Mappers.getMapper(CityRegionMapper.class);

    CityRegion toCityRegion(CityRegionDto cityRegionDto);

    CityRegionDto toCityRegionDto(CityRegion cityRegion);

    List<CityRegion> toList(List<CityRegionDto> cityRegionDtoList);

    List<CityRegionDto> toListDto(List<CityRegion> cityRegionList);

}
