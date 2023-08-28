package com.fon.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class CitySubregion extends Location {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_region_id")
    private CityRegion cityRegion;

}
