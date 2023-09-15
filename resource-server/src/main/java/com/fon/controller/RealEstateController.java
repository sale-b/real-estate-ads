package com.fon.controller;

import com.fon.DAO.ImageRepository;
import com.fon.dto.AutocompleteValuesDto;
import com.fon.dto.PageRequestDto;
import com.fon.dto.RealEstateDetailsDto;
import com.fon.entity.RealEstate;
import com.fon.entity.enumeration.EventAction;
import com.fon.service.CitySubregionService;
import com.fon.service.EventService;
import com.fon.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/real-estate")
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    CitySubregionService citySubregionService;

    @Autowired
    EventService eventService;

    @GetMapping("/autocomplete-fields")
    public ResponseEntity<AutocompleteValuesDto> getAutocompleteValues() {
        return ResponseEntity.status(HttpStatus.OK).body(realEstateService.getAutocompleteValues());
    }

    @PutMapping()
    public ResponseEntity<RealEstate> saveRealEstate(@ModelAttribute("model") String request,
                                                     @RequestParam(value = "images", required = false) List<MultipartFile> fileList, Principal principal) throws IOException {

        RealEstate realEstate = realEstateService.save(request, fileList, principal.getName());
        try {
            Runnable runnable = () -> {
                eventService.sendEvent(eventService.createEvent(realEstate, EventAction.UPDATE));
            };
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(realEstate);
    }

    @PostMapping("/page")
    public ResponseEntity<Page<RealEstateDetailsDto>> getRealEstates(
            @RequestBody PageRequestDto pageRequestDto) {
        System.out.println(pageRequestDto);
        Page<RealEstateDetailsDto> realEstates = realEstateService.getRealEstates(pageRequestDto);
        return ResponseEntity.ok(realEstates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealEstateDetailsDto> findAll(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(realEstateService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id, Principal principal) {
        realEstateService.deleteById(id, principal.getName());
        try {
            Runnable runnable = () -> {
                eventService.sendEvent(eventService.createEvent(RealEstate.builder().id(id).build(), EventAction.DELETE));
            };
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
