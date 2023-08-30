package com.fon.service;

import com.fon.entity.*;
import com.fon.entity.enumeration.*;
import com.fon.util.EnumUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class RealEstateService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RealEstate> getRealEstates(Filter filter) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RealEstate> criteriaQuery = criteriaBuilder.createQuery(RealEstate.class);
        Root<RealEstate> root = criteriaQuery.from(RealEstate.class);
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null) {

            if (filter.getHasPictures() != null && filter.getHasPictures()) {
                predicates.add(criteriaBuilder.equal(root.get("hasPictures"), true));
            }
            if (filter.getLocation() != null && filter.getLocation() != null) {
                if ((filter.getLocation() == null || filter.getMicroLocation().isEmpty())) {

                    Join<CityRegion, City> cityJoin = root.join("location").join("cityRegion").join("city");
                    predicates.add(cityJoin.get("id").in(filter.getLocation()));
                }
            }

            if (filter.getMicroLocation() != null && !filter.getMicroLocation().isEmpty()) {
                List<Long> citySubregionIds = filter.getMicroLocation().stream()
                        .map(CitySubregion::getId)
                        .collect(Collectors.toList());

                Join<CitySubregion, City> cityRegionJoin = root.join("location");
                predicates.add(cityRegionJoin.get("id").in(citySubregionIds));
            }

            if (filter.getPriceLess() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getPriceLess()));
            }
            if (filter.getPriceHigher() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getPriceHigher()));
            }
            if (filter.getSpaceAreaLess() != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("livingSpaceArea"), filter.getSpaceAreaLess()));
            }
            if (filter.getSpaceAreaHigher() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("livingSpaceArea"), filter.getSpaceAreaHigher()));
            }

            if (filter.getRoomsNumberHigher() != null) {
                String minRoomsNumber = EnumUtils.fromLabel(RoomsNumber.class, filter.getRoomsNumberHigher()).toString();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("roomsNumber").as(String.class), minRoomsNumber));
            }

            if (filter.getRoomsNumberLess() != null) {
                String maxRoomsNumber = EnumUtils.fromLabel(RoomsNumber.class, filter.getRoomsNumberLess()).toString();
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("roomsNumber").as(String.class), maxRoomsNumber));
            }

            if (filter.getAdType() != null && !filter.getAdType().isEmpty()) {
                Expression<AdType> adTypeExpression = root.get("adType");
                predicates.add(adTypeExpression.in(filter.getAdType()));
            }

            if (filter.getType() != null && !filter.getType().isEmpty()) {
                Expression<RealEstateType> typeExpression = root.get("realEstateType");
                predicates.add(typeExpression.in(filter.getType()));
            }

            if (filter.getHeatingType() != null && !filter.getHeatingType().isEmpty()) {
                Expression<HeatingType> heatingTypeExpression = root.get("heatingType");
                predicates.add(heatingTypeExpression.in(filter.getHeatingType()));
            }

            if (filter.getFurniture() != null && !filter.getFurniture().isEmpty()) {
                Expression<FurnitureType> furnitureTypeExpression = root.get("furnitureType");
                predicates.add(furnitureTypeExpression.in(filter.getFurniture()));
            }

            if (filter.getFloor() != null && !filter.getFloor().isEmpty()) {
                Expression<Floor> floorExpression = root.get("floor");
                predicates.add(floorExpression.in(filter.getFloor()));
            }
        } else {
            return null;
        }

        criteriaQuery.select(root)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(criteriaBuilder.desc(root.get("id")));

        return entityManager.createQuery(criteriaQuery).getResultList();

    }
}
