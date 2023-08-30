package com.fon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fon.entity.enumeration.*;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Filter extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "city_id")
    private Long location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", insertable = false, updatable = false)
    private City city;

    @ManyToMany
    @Column(length = 1000)
    private List<CitySubregion> microLocation;

    private Double priceLess;
    private Double priceHigher;
    private Float spaceAreaLess;
    private Float spaceAreaHigher;
    private String roomsNumberLess;
    private String roomsNumberHigher;

    @ElementCollection(targetClass = RealEstateType.class)
    @CollectionTable(name = "filter_real_estate_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_real_estate_type_index", columnList = "real_estate_type")})
    @Column(name = "real_estate_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<RealEstateType> type;

    @ElementCollection(targetClass = AdType.class)
    @CollectionTable(name = "filter_ad_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_ad_type_index", columnList = "ad_type")})
    @Column(name = "ad_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<AdType> adType;

    @ElementCollection(targetClass = HeatingType.class)
    @CollectionTable(name = "filter_heating_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_heating_type_index", columnList = "heating_type")})
    @Column(name = "heating_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<HeatingType> heatingType;

    @ElementCollection(targetClass = Floor.class)
    @CollectionTable(name = "filter_floor", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_floor_index", columnList = "floor")})
    @Column(name = "floor", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Floor> floor;

    @ElementCollection(targetClass = FurnitureType.class)
    @CollectionTable(name = "filter_furniture_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_furniture_type_index", columnList = "furniture_type")})
    @Column(name = "furniture_type", nullable = false)
    private Set<FurnitureType> furniture;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Boolean hasPictures;

    @Column(nullable = false)
    private Boolean subscribed;

    @JsonProperty("user")
    private void getIdFromUserObject(Map<String, String> user) {
        userId = Long.parseLong(user.get("id"));
    }

}