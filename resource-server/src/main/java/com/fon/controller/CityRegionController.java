package com.fon.controller;

import com.fon.dto.CityDto;
import com.fon.dto.CityRegionDto;
import com.fon.service.CityRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city-region")
public class CityRegionController {

    @Autowired
    CityRegionService cityRegionService;

    @GetMapping("/{id}")
    public ResponseEntity<List<CityRegionDto>> findAll(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(cityRegionService.findByCityId(id));
    }

    @PostMapping()
    public ResponseEntity<List<CityRegionDto>> findByCityIdIn(@RequestBody List<CityDto> cities) {
        System.out.println("Reqest:");
        for (int i = 0; i < cities.size(); i++) {
            System.out.println(cities.get(i).getName());
        }
        System.out.println("Response:");
        List<CityRegionDto> cityRegionDtos = cityRegionService.findByCityIdIn(cities);
        for (CityRegionDto cityRegionDto:cityRegionDtos) {
            System.out.println(cityRegionDto.getName());
        }
        return ResponseEntity.status(HttpStatus.OK).body(cityRegionDtos);
    }
}
