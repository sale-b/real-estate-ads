package com.fon.service;

import com.fon.DAO.CityRegionRepository;
import com.fon.dto.CityDto;
import com.fon.dto.CityRegionDto;
import com.fon.entity.CityRegion;
import com.fon.mapper.CityRegionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CityRegionService {

    private final CityRegionRepository cityRegionRepository;

    public CityRegionService(CityRegionRepository cityRegionRepository) {
        this.cityRegionRepository = cityRegionRepository;
    }

    public Optional<CityRegion> findById(Long id) {
        return cityRegionRepository.findById(id);
    }

    public List<CityRegionDto> findByCityId(Long id) {
        return CityRegionMapper.INSTANCE.toListDto(cityRegionRepository.findByCityId(id));
    }


    public List<CityRegionDto> findByCityIdIn(List<CityDto> cities) {
        return CityRegionMapper.INSTANCE.toListDto(cityRegionRepository.findByCityIdIn(
                cities.stream()
                        .map(CityDto::getId)
                        .collect(Collectors.toList())
        ));
    }
}
