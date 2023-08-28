package com.fon.controller;

import com.fon.dto.CityDto;
import com.fon.dto.CitySubregionDto;
import com.fon.service.CitySubregionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/city-subregion")
public class CitySubregionController {

    @Autowired
    CitySubregionService citySubregionService;

    @GetMapping("/{id}")
    public ResponseEntity<List<CitySubregionDto>> findAll(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(citySubregionService.findByCityRegionId(id));
    }

    @PostMapping()
    public ResponseEntity<List<CitySubregionDto>> findByCityIdIn(@RequestBody List<CityDto> cities) {
        List<CitySubregionDto> citySubregionDtos = citySubregionService.findByCityIdIn(cities);
        return ResponseEntity.status(HttpStatus.OK).body(citySubregionDtos);
    }
}
