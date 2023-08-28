package com.fon.DAO;

import com.fon.entity.CityRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CityRegionRepository extends JpaRepository<CityRegion, Long> {
    List<CityRegion> findByCityId(Long cityId);
    List<CityRegion> findByCityIdIn(List<Long> cityIds);
}