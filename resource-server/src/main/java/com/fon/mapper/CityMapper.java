package com.fon.mapper;

import com.fon.dto.CityDto;
import com.fon.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    City toCity(CityDto cityDto);

    CityDto toCityDto(City city);

    List<City> toList(List<CityDto> cityDtoList);

    List<CityDto> toListDto(List<City> cityList);

}
