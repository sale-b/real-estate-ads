package com.fon.service;

import com.fon.DAO.CitySubregionRepository;
import com.fon.dto.CityDto;
import com.fon.dto.CitySubregionDto;
import com.fon.entity.CitySubregion;
import com.fon.mapper.CitySubregionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class CitySubregionService {


    @Autowired
    private CitySubregionRepository citySubregionRepository;

    public Optional<CitySubregion> findById(Long id) {
        return citySubregionRepository.findById(id);
    }

    public List<CitySubregionDto> findByCityRegionId(Long id) {
        return CitySubregionMapper.INSTANCE.toListDto(citySubregionRepository.findByCityRegionId(id));
    }

    public List<CitySubregionDto> findByCityIdIn(List<CityDto> cities) {
        return CitySubregionMapper.INSTANCE.toListDto(citySubregionRepository.findByCityIdInOrderByCityRegionNameAsc(
                cities.stream()
                        .map(CityDto::getId)
                        .collect(Collectors.toList())
        ));
    }
}
