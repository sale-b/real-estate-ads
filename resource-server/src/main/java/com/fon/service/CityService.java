package com.fon.service;

import com.fon.DAO.CityRepository;
import com.fon.dto.CityDto;
import com.fon.entity.City;
import com.fon.mapper.CityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }

    public List<CityDto> findAll() {
        return CityMapper.INSTANCE.toListDto(cityRepository.findAll());
    }
}
