package com.fon.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
public abstract class Location {
    @Id
    private Long id;

    private String name;

    @Column(name = "geolocation")
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "geolocation_x")),
            @AttributeOverride(name = "y", column = @Column(name = "geolocation_y"))
    })
    private GeoLocation geoLocation;

    private Integer zoom;

    @Column(name = "`order`")
    private Long order;
}