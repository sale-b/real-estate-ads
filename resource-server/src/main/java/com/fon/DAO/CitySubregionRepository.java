package com.fon.DAO;

import com.fon.entity.CitySubregion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CitySubregionRepository extends JpaRepository<CitySubregion, Long> {
    List<CitySubregion> findByCityRegionId(Long cityRegionId);

//    List<CitySubregion> findByCityRegion_City_IdInOrderByCityRegion_Name(List<Long> cityIds);
@Query("SELECT cs FROM CitySubregion cs WHERE cs.cityRegion.city.id IN :cityIds ORDER BY cs.name ASC")
List<CitySubregion> findByCityIdInOrderByCityRegionNameAsc(@Param("cityIds") List<Long> cityIds);
}