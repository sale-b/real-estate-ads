package com.fon.DAO;

import com.fon.entity.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

}