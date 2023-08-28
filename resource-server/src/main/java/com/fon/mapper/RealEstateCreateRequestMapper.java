package com.fon.mapper;

import com.fon.dto.RealEstateCreateRequestDto;
import com.fon.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RealEstateCreateRequestMapper {

    RealEstateCreateRequestMapper INSTANCE = Mappers.getMapper(RealEstateCreateRequestMapper.class);

    @Mapping(target = "heatingType", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.HeatingType.class, realEstateCreateRequestDto.getHeatingType()))")
    @Mapping(target = "adType", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.AdType.class, realEstateCreateRequestDto.getAdType()))")
    @Mapping(target = "realEstateType", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.RealEstateType.class, realEstateCreateRequestDto.getRealEstateType()))")
    @Mapping(target = "floor", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.Floor.class, realEstateCreateRequestDto.getFloor()))")
    @Mapping(target = "furnitureType", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.FurnitureType.class, realEstateCreateRequestDto.getFurnitureType()))")
    @Mapping(target = "roomsNumber", expression = "java(com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.RoomsNumber.class, realEstateCreateRequestDto.getRoomsNumber()))")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "images", expression = "java(new java.util.ArrayList<>())")
    RealEstate toRealEstate(RealEstateCreateRequestDto realEstateCreateRequestDto);


}
