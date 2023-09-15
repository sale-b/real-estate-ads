package com.fon.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fon.entity.RealEstate;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealEstateEvent extends BaseEvent {
    private Long id;
    private RealEstate realEstate;
}
