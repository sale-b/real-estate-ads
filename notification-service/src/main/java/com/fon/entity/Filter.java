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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", referencedColumnName = "id", insertable = false, updatable = false)
    private City city;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(length = 1000)
    private List<CitySubregion> microLocation;

    private Double priceLess;
    private Double priceHigher;
    private Float spaceAreaLess;
    private Float spaceAreaHigher;
    private String roomsNumberLess;
    private String roomsNumberHigher;

    @ElementCollection(targetClass = RealEstateType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_real_estate_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_real_estate_type_index", columnList = "real_estate_type")})
    @Column(name = "real_estate_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<RealEstateType> type;

    @ElementCollection(targetClass = AdType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_ad_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_ad_type_index", columnList = "ad_type")})
    @Column(name = "ad_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<AdType> adType;

    @ElementCollection(targetClass = HeatingType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_heating_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_heating_type_index", columnList = "heating_type")})
    @Column(name = "heating_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<HeatingType> heatingType;

    @ElementCollection(targetClass = Floor.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_floor", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_floor_index", columnList = "floor")})
    @Column(name = "floor", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Floor> floor;

    @ElementCollection(targetClass = FurnitureType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "filter_furniture_type", joinColumns = @JoinColumn(name = "filter_id"),
            indexes = {@Index(name = "filter_furniture_type_index", columnList = "furniture_type")})
    @Column(name = "furniture_type", nullable = false)
    private Set<FurnitureType> furniture;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Boolean hasPictures;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean subscribed;

    @JsonProperty("user")
    private void getEmailFromUserObject(Map<String, String> user) {
        this.userEmail = user.get("email");
        this.userId = user.get("id");
    }

}