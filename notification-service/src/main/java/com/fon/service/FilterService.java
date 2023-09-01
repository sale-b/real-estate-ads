package com.fon.service;

import com.fon.DAO.FilterRepository;
import com.fon.entity.CitySubregion;
import com.fon.entity.Filter;
import com.fon.entity.RealEstate;
import com.fon.entity.enumeration.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FilterService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FilterRepository filterRepository;

    public List<Filter> getFilters(RealEstate realEstate) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Filter> criteriaQuery = criteriaBuilder.createQuery(Filter.class);
        Root<Filter> rootFilter = criteriaQuery.from(Filter.class);
        List<Predicate> predicates = new ArrayList<>();

        if (realEstate != null) {

            predicates.add(criteriaBuilder.lessThan(
                    rootFilter.get("createdOn"), criteriaBuilder.literal(realEstate.getCreatedOn())
            ));

            if (!realEstate.getHasPictures()) {
                predicates.add(criteriaBuilder.equal(rootFilter.get("hasPictures"), false));
            }

            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(rootFilter.get("location"), realEstate.getLocation().getCityRegion().getCity().getId()),
                    criteriaBuilder.isNull(rootFilter.get("location"))
            ));

            Join<Filter, CitySubregion> microLocationsJoin = rootFilter.join("microLocation", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(microLocationsJoin.get("id"), realEstate.getLocation().getId()),
                    criteriaBuilder.isNull(microLocationsJoin)
            ));

            Expression<Double> getPriceHigher = rootFilter.get("priceHigher");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.lessThanOrEqualTo(getPriceHigher, realEstate.getPrice()),
                    criteriaBuilder.isNull(getPriceHigher)
            ));

            Expression<Double> getPriceLess = rootFilter.get("priceLess");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.greaterThanOrEqualTo(getPriceLess, realEstate.getPrice()),
                    criteriaBuilder.isNull(getPriceLess)
            ));

            Expression<Float> getSpaceAreaHigher = rootFilter.get("spaceAreaHigher");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.lessThanOrEqualTo(getSpaceAreaHigher, realEstate.getLivingSpaceArea()),
                    criteriaBuilder.isNull(getSpaceAreaHigher)
            ));

            Expression<Float> getSpaceAreaLess = rootFilter.get("spaceAreaLess");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.greaterThanOrEqualTo(getSpaceAreaLess, realEstate.getLivingSpaceArea()),
                    criteriaBuilder.isNull(getSpaceAreaLess)
            ));

            Expression<String> getRoomsNumberHigher = rootFilter.get("roomsNumberHigher");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.lessThanOrEqualTo(getRoomsNumberHigher, realEstate.getRoomsNumber().getLabel()),
                    criteriaBuilder.isNull(getRoomsNumberHigher)
            ));

            Expression<String> getRoomsNumberLess = rootFilter.get("roomsNumberLess");
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.greaterThanOrEqualTo(getRoomsNumberLess, realEstate.getRoomsNumber().getLabel()),
                    criteriaBuilder.isNull(getRoomsNumberLess)
            ));

            Join<Filter, AdType> adTypeJoin = rootFilter.join("adType", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(adTypeJoin, realEstate.getAdType()),
                    criteriaBuilder.isNull(adTypeJoin)
            ));

            Join<Filter, RealEstateType> realEstateTypeJoin = rootFilter.join("type", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(realEstateTypeJoin, realEstate.getRealEstateType()),
                    criteriaBuilder.isNull(realEstateTypeJoin)
            ));

            Join<Filter, HeatingType> heatingTypeJoin = rootFilter.join("heatingType", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(heatingTypeJoin, realEstate.getHeatingType()),
                    criteriaBuilder.isNull(heatingTypeJoin)
            ));

            Join<Filter, FurnitureType> furnitureJoin = rootFilter.join("furniture", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(furnitureJoin, realEstate.getFurnitureType()),
                    criteriaBuilder.isNull(furnitureJoin)
            ));

            Join<Filter, Floor> floorsJoin = rootFilter.join("floor", JoinType.LEFT);
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.equal(floorsJoin, realEstate.getFloor()),
                    criteriaBuilder.isNull(floorsJoin)
            ));

        } else {
            return null;
        }

        criteriaQuery.select(rootFilter).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public Filter save(Filter filter){
        return filterRepository.save(filter);
    }

    public void delete(Filter filter) {
        filterRepository.delete(filter);
    }
}
