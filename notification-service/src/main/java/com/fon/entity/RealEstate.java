package com.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fon.entity.enumeration.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealEstate extends BaseEntity {

    @Id
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;
    @NotNull
    @Column(nullable = false)
    private String phone;
    @NotNull
    @Column(nullable = false)
    private Double price;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdType adType;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RealEstateType realEstateType;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomsNumber roomsNumber;

    @Enumerated(EnumType.STRING)
    private Floor floor;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_subregion_id")
    @NotNull
    private CitySubregion location;

    @Column(name = "geolocation")
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "geolocation_x", nullable = false)),
            @AttributeOverride(name = "y", column = @Column(name = "geolocation_y", nullable = false))
    })
    @NotNull
    private GeoLocation geoLocation;

    @NotNull
    @Column(nullable = false)
    private Float livingSpaceArea;

    @Enumerated(EnumType.STRING)
    private FurnitureType furnitureType;

    @Enumerated(EnumType.STRING)
    private HeatingType heatingType;
    @NotNull
    @Column(nullable = false)
    private Boolean hasPictures;


}
