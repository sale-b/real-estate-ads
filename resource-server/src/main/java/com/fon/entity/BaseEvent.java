package com.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fon.entity.enumeration.EventAction;
import com.fon.entity.enumeration.RoomsNumber;
import com.fon.util.BaseEntityListener;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
@EntityListeners(BaseEntityListener.class)
@MappedSuperclass
public abstract class BaseEvent extends BaseEntity{

    @Column(columnDefinition = "boolean default false", nullable = false)
    @JsonIgnore
    private Boolean sent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventAction eventAction;

}
