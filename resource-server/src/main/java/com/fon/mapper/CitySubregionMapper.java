package com.fon.mapper;

import com.fon.dto.CitySubregionDto;
import com.fon.entity.CitySubregion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CitySubregionMapper {

    CitySubregionMapper INSTANCE = Mappers.getMapper(CitySubregionMapper.class);

    CitySubregion toCitySubregion(CitySubregionDto citySubregionDto);

    CitySubregionDto toCitySubregionDto(CitySubregion citySubregion);

    List<CitySubregion> toList(List<CitySubregionDto> citySubregionDtoList);

    List<CitySubregionDto> toListDto(List<CitySubregion> citySubregionList);

}
