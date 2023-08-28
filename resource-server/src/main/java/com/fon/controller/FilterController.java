package com.fon.controller;

import com.fon.dto.FilterDto;
import com.fon.entity.Filter;
import com.fon.mapper.FilterMapper;
import com.fon.service.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/filter")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class FilterController {

    @Autowired
    FilterService filterService;

    @PostMapping()
    public ResponseEntity<FilterDto> save(@RequestBody FilterDto filterDto) {
        Filter filter = FilterMapper.INSTANCE.toFilter(filterDto);
        return ResponseEntity.status(HttpStatus.OK).body(filterService.save(filter));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FilterDto>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(filterService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        filterService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


}
