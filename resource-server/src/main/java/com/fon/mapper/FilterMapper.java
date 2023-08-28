package com.fon.mapper;

import com.fon.dto.CityDto;
import com.fon.dto.FilterDto;
import com.fon.dto.NotificationDto;
import com.fon.entity.Filter;
import com.fon.entity.Notification;
import com.fon.entity.User;
import com.fon.entity.enumeration.*;
import com.fon.service.NotificationService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(imports = {Collectors.class, User.class, CityDto.class, RealEstateType.class, AdType.class,
        HeatingType.class, Floor.class, FurnitureType.class},
        componentModel = "spring")
public interface FilterMapper {

    FilterMapper INSTANCE = Mappers.getMapper(FilterMapper.class);

    @Mapping(target = "adType", expression = "java(filterDto.getAdType() != null ? filterDto.getAdType().stream().map(adType->com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.AdType.class, adType)).collect(Collectors.toSet()) : null)")
    @Mapping(target = "type", expression = "java(filterDto.getType() != null ? filterDto.getType().stream().map(realestateType->com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.RealEstateType.class, realestateType)).collect(Collectors.toSet()) : null)")
    @Mapping(target = "heatingType", expression = "java(filterDto.getHeatingType() != null ? filterDto.getHeatingType().stream().map(heatingType->com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.HeatingType.class, heatingType)).collect(Collectors.toSet()) : null)")
    @Mapping(target = "floor", expression = "java(filterDto.getFloor() != null ? filterDto.getFloor().stream().map(floor->com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.Floor.class, floor)).collect(Collectors.toSet()) : null)")
    @Mapping(target = "furniture", expression = "java(filterDto.getFurniture() != null ? filterDto.getFurniture().stream().map(furniture->com.fon.util.EnumUtils.fromLabel(com.fon.entity.enumeration.FurnitureType.class, furniture)).collect(Collectors.toSet()) : null)")
    @Mapping(target = "location", expression = "java(filterDto.getLocation().getId())")
    @Mapping(target = "user", expression = "java(User.builder().id(filterDto.getUserId()).build())")
    Filter toFilter(FilterDto filterDto);

    @Mapping(target = "location", expression = "java(CityDto.builder().id(filter.getLocation()).build())")
    @Mapping(target = "userId", source = "filter.user.id")
    @Mapping(target = "type", expression = "java(filter.getType() != null ? filter.getType().stream().map(RealEstateType::getLabel).collect(Collectors.toSet()) : null)")
    @Mapping(target = "adType", expression = "java(filter.getAdType() != null ? filter.getAdType().stream().map(AdType::getLabel).collect(Collectors.toSet()) : null)")
    @Mapping(target = "heatingType", expression = "java(filter.getHeatingType() != null ? filter.getHeatingType().stream().map(HeatingType::getLabel).collect(Collectors.toSet()) : null)")
    @Mapping(target = "floor", expression = "java(filter.getFloor() != null ? filter.getFloor().stream().map(Floor::getLabel).collect(Collectors.toSet()) : null)")
    @Mapping(target = "furniture", expression = "java(filter.getFurniture() != null ? filter.getFurniture().stream().map(FurnitureType::getLabel).collect(Collectors.toSet()) : null)")
    @Mapping(target = "newNotificationsCount", expression = "java(calculateNewNotificationCount(notificationService, filter))")
    FilterDto toFilterDto(Filter filter, @Context NotificationService notificationService);

    List<Filter> toList(List<FilterDto> filterDtoList);

    List<FilterDto> toListDto(List<Filter> filterList, @Context NotificationService notificationService);

    default List<NotificationDto> mapNotifications(List<Notification> notifications) {
        return NotificationMapper.INSTANCE.toListDto(notifications);
    }

    default CityDto map(Long value) {
        return CityDto.builder().id(value).build();
    }

    default long calculateNewNotificationCount(@Context NotificationService notificationService, Filter filter) {
        // Use the injected notificationService to execute the count query
        return notificationService.countUnseenNotifications(filter.getId());
    }

}
