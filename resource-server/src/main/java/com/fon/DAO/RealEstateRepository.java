package com.fon.DAO;

import com.fon.entity.RealEstate;
import com.fon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    Page<RealEstate> findAll(Specification<RealEstate> spec, Pageable pageable);

    Page<RealEstate> findByUser(User user, Pageable pageable);
}