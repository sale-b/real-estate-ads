package com.fon.mapper;

import com.fon.dto.NotificationDto;
import com.fon.dto.RealEstateDetailsDto;
import com.fon.entity.Notification;
import com.fon.entity.RealEstate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "realEstate", expression = "java(mapRealEstate(notification.getRealEstate()))")
    NotificationDto toNotificationDto(Notification notification);

    List<NotificationDto> toListDto(List<Notification> notificationList);

    default RealEstateDetailsDto mapRealEstate(RealEstate realEstate) {
        return RealEstateDetailsMapper.INSTANCE.toRealEstateDetailsDto(realEstate);
    }

}
