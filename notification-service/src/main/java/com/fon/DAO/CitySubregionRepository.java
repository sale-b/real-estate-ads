package com.fon.DAO;

import com.fon.entity.CitySubregion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CitySubregionRepository extends JpaRepository<CitySubregion, Long> {

}