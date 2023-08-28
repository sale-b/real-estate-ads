package com.fon.entity;

import com.fon.entity.converter.*;
import com.fon.entity.enumeration.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "title"}))
public class Filter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "city_id")
    private Long location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id", insertable = false, updatable = false)
    private City city;

    @Convert(converter = MicroLocationSetConverter.class)
    @Column(length = 1000)
    private List<CitySubregion> microLocation;

    private Double priceLess;
    private Double priceHigher;
    private Float spaceAreaLess;
    private Float spaceAreaHigher;
    private String roomsNumberLess;
    private String roomsNumberHigher;

    @Convert(converter = RealEstateTypeSetConverter.class)
    @Column(length = 1000)
    private Set<RealEstateType> type;

    @Convert(converter = AdTypeSetConverter.class)
    @Column(length = 1000)
    private Set<AdType> adType;

    @Convert(converter = HeatingTypeSetConverter.class)
    @Column(length = 1000)
    private Set<HeatingType> heatingType;

    @Convert(converter = FloorSetConverter.class)
    @Column(length = 1000)
    private Set<Floor> floor;

    @Convert(converter = FurnitureTypeSetConverter.class)
    @Column(length = 1000)
    private Set<FurnitureType> furniture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "filter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notifications= new ArrayList<>();

    private Boolean hasPictures;

    private String title;
    private Boolean subscribed;

//    @AttributeOverrides({
//            @AttributeOverride(name = "x", column = @Column(name = "geolocation_x")),
//            @AttributeOverride(name = "y", column = @Column(name = "geolocation_y"))
//    })
//    private List<GeoLocation> coordinates;
}