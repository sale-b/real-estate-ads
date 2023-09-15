package com.fon.message;

import com.fon.entity.BaseEntity;
import com.fon.entity.enumeration.EventAction;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@MappedSuperclass
public abstract class BaseEvent extends BaseEntity {

    private EventAction eventAction;

}
