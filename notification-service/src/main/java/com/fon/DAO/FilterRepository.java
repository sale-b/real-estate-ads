package com.fon.DAO;

import com.fon.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
}