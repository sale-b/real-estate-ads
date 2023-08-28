package com.fon.mapper;

import com.fon.dto.RealEstateDetailsDto;
import com.fon.entity.Image;
import com.fon.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper( imports = {ArrayList.class, Collectors.class, Image.class} )
public interface RealEstateDetailsMapper {

    RealEstateDetailsMapper INSTANCE = Mappers.getMapper(RealEstateDetailsMapper.class);

    @Mapping(target = "roomsNumber", expression = "java(realEstate.getRoomsNumber().getLabel())")
    @Mapping(target = "adType", source = "realEstate.adType.label")
    @Mapping(target = "userId", source = "realEstate.user.id")
    @Mapping(target = "furnitureType", source = "realEstate.furnitureType.label")
    @Mapping(target = "realEstateType", source = "realEstate.realEstateType.label")
    @Mapping(target = "heatingType", source = "realEstate.heatingType.label")
    @Mapping(target = "floor", expression = "java(realEstate.getFloor().getLabel())")
    @Mapping(target = "tittle", expression =  "java(realEstate.getTitle())")
    @Mapping(target = "city", source =  "realEstate.location.cityRegion.city.name")
    @Mapping(target = "cityRegion", source =  "realEstate.location.cityRegion.name")
    @Mapping(target = "citySubregion ", source =  "realEstate.location.name")
    @Mapping(target = "location", expression = "java(java.lang.String.format(\"%s - %s - %s\", realEstate.getLocation().getCityRegion().getCity().getName(), realEstate.getLocation().getCityRegion().getName(), realEstate.getLocation().getName()))")
    @Mapping(target = "imageUrl", expression = "java(realEstate.getHasPictures()?realEstate.getImages().get(0).getImageUrl() : null)")
    @Mapping(target = "images", expression = "java(realEstate.getHasPictures()?realEstate.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList()) : new ArrayList<>())")
    @Mapping(target = "geoLocationZoom", source = "location.zoom")
    @Mapping(target = "geoLocationCoordinates", source = "geoLocation")
    RealEstateDetailsDto toRealEstateDetailsDto(RealEstate realEstate);


}
