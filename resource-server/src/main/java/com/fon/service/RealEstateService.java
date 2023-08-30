package com.fon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.DAO.RealEstateRepository;
import com.fon.dto.*;
import com.fon.entity.*;
import com.fon.entity.enumeration.*;
import com.fon.mapper.RealEstateCreateRequestMapper;
import com.fon.mapper.RealEstateDetailsMapper;
import com.fon.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;
    @Autowired
    private CitySubregionService citySubregionService;
    @Autowired
    private CityService cityService;
    @Autowired
    private ImageService imageService;
    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;

    @Value("${baseUrl}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final static String DIRECTORY_PATH = "C:\\images";


    public RealEstateDetailsDto findById(Long id) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(id);
        return RealEstateDetailsMapper.INSTANCE.toRealEstateDetailsDto(
                realEstate.orElseThrow(() -> new EntityNotFoundException(String.format("Entity with ID: %d not found.", id))));

    }

    public RealEstate save(RealEstate realEstate) {
        if (realEstate.getId() != null) {
            RealEstate oldRealEstate = realEstateRepository.findById(realEstate.getId()).get();
            realEstate.setCreatedOn(oldRealEstate.getCreatedOn());
            if (realEstate.getUser().getId() != oldRealEstate.getUser().getId()) {
                throw new RuntimeException("Unauthorized to edit ad!");
            }
        }
        CitySubregion citySubregion = citySubregionService.findById(realEstate.getLocation().getId()).get();
        realEstate.setLocation(citySubregion);
        realEstate = realEstateRepository.save(realEstate);
        eventService.sendEvent(realEstate);
        return realEstate;
    }

    public RealEstate save(String request, List<MultipartFile> fileList, String userEmail) throws IOException {
        List<String> fileNames = new ArrayList<>();

        try {
            RealEstateCreateRequestDto realEstateDto = objectMapper.readValue(request, RealEstateCreateRequestDto.class);

            RealEstate realEstate = RealEstateCreateRequestMapper.INSTANCE.toRealEstate(realEstateDto);

            if (realEstate.getId() != null) {
                Optional<RealEstate> oldRealEstate = realEstateRepository.findById(realEstate.getId());
                if (oldRealEstate.isPresent()) {
                    if (!oldRealEstate.get().getUser().getEmail().equals(userEmail)) {
                        throw new RuntimeException("Not authorized user!");
                    } else {
                        realEstate.setUser(oldRealEstate.get().getUser());
                    }
                }
            } else {
                realEstate.setUser(userService.findByEmail(userEmail).get());
            }

            realEstate.setLocation(citySubregionService.findById(realEstateDto.getLocation()).get());

            // Handle the image file
            if (!realEstate.getHasPictures()) {
                handleSaveImagesList(realEstate, false);
                realEstate = save(realEstate);
            } else {
                if (fileList != null && !fileList.isEmpty()) {
                    handleSaveImagesList(realEstate, false);
                    realEstate = save(realEstate);

                    for (MultipartFile multipartFile : fileList) {
                        if (!multipartFile.isEmpty()) {
                            // Save the image file to file storage
                            String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
                            fileNames.add(fileName);
                            String imageUrl = saveImageToStorage(multipartFile, fileName);

                            // Create a new Image object and associate it with the RealEstate
                            Image image = createImage(baseUrl + imageUrl, realEstate);

                            // Add the Image to the RealEstate's images list
                            realEstate.addImage(image);
                        }
                    }
                } else {
                    handleSaveImagesList(realEstate, true);
                }
            }

            return realEstate;

        } catch (IOException e) {
            for (String fileName : fileNames) {
                Files.deleteIfExists(Paths.get(DIRECTORY_PATH, fileName));
            }
            e.printStackTrace();
            throw new RuntimeException("Error saving images.");
        }
    }

    private Image createImage(String imageUrl, RealEstate realEstate) {
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setRealEstate(realEstate);
        imageService.save(image);
        return image;
    }

    private void deleteImagesFromStorage(List<Image> images) {
        for (Image image : images) {
            try {
                String fileName = image.getImageUrl().substring(image.getImageUrl().lastIndexOf("/") + 1);
                Path imagePath = Paths.get(DIRECTORY_PATH, fileName);
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Error deleting image: " + image.getImageUrl(), e);
            }
        }
    }

    private void deleteImages(List<Image> images) {
        deleteImagesFromStorage(images);
    }


    private RealEstate handleSaveImagesList(RealEstate realEstate, boolean isEmptyList) {
        if (realEstate.getId() != null) {
            Optional<RealEstate> oldRealEstate = realEstateRepository.findById(realEstate.getId());
            if (oldRealEstate.isPresent()) {
                if (isEmptyList) {
                    realEstate.setImages(oldRealEstate.get().getImages());
                    realEstate = save(realEstate);
                } else {
                    deleteImages(oldRealEstate.get().getImages());
                }
            } else {
                throw new RuntimeException(String.format("Entity with ID: %d not found.", realEstate.getId()));
            }
        }
        return realEstate;
    }

    private String saveImageToStorage(MultipartFile multipartFile, String fileName) throws IOException {

        // Create the directory if it doesn't exist
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = Paths.get(DIRECTORY_PATH, fileName).toString();
        File destinationFile = new File(filePath);
        multipartFile.transferTo(destinationFile);

//        testRollback(multipartFile);

        return String.format("/images/%s", fileName);
    }

    private void testRollback(MultipartFile multipartFile) throws IOException {
        if (multipartFile.getOriginalFilename().equals("Screenshot (16).png")) {
            throw new IOException("Error");
        }
    }

    public void deleteById(Long id, String userEmail) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(id);
        if (realEstate.isPresent() && realEstate.get().getUser().getEmail().equals(userEmail)) {
            realEstateRepository.delete(realEstate.get());
            deleteImages(realEstate.get().getImages());
        } else {
            throw new RuntimeException(String.format("Entity with ID: %d not found, or user with email: %s has no right do delete it", id, userEmail));
        }
    }

    public AutocompleteValuesDto getAutocompleteValues() {
        return AutocompleteValuesDto.builder()
                .floors(EnumUtils.getLabels(Floor.class))
                .adTypes(EnumUtils.getLabels(AdType.class))
                .furnitureTypes(EnumUtils.getLabels(FurnitureType.class))
                .heatingTypes(EnumUtils.getLabels(HeatingType.class))
                .realEstateTypes(EnumUtils.getLabels(RealEstateType.class))
                .roomsNumbers(EnumUtils.getLabels(RoomsNumber.class))
                .cities(cityService.findAll())
                .build();
    }

    public Page<RealEstateDetailsDto> getRealEstates(PageRequestDto pageRequestDto) {
        Specification<RealEstate> spec = (root, query, criteriaBuilder) -> {
            if (pageRequestDto.getFilters() != null) {
                List<Predicate> predicates = buildCriteria(pageRequestDto, root, criteriaBuilder);
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            } else {
                return null;
            }
        };

        Sort sort = Sort.by(Sort.Direction.DESC, "createdOn");
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), sort);

        Page<RealEstate> page = realEstateRepository.findAll(spec, pageable);

        List<RealEstateDetailsDto> dtoList = page.getContent().stream()
                .map(RealEstateDetailsMapper.INSTANCE::toRealEstateDetailsDto)
                .collect(Collectors.toList());
        Page<RealEstateDetailsDto> pageDto = new PageImpl<>(dtoList, pageable, page.getTotalElements());

        return pageDto;
    }

    private List<Predicate> buildCriteria(PageRequestDto pageRequestDto, Root<RealEstate> root, CriteriaBuilder
            criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (pageRequestDto.getFilters().getUserId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), pageRequestDto.getFilters().getUserId()));
        }

        if (pageRequestDto.getFilters().getHasPictures() != null && pageRequestDto.getFilters().getHasPictures()) {
            predicates.add(criteriaBuilder.equal(root.get("hasPictures"), true));
        }
        if (pageRequestDto.getFilters().getLocation() != null && pageRequestDto.getFilters().getLocation().getId() != null) {
            if ((pageRequestDto.getFilters().getMicroLocation() == null || pageRequestDto.getFilters().getMicroLocation().isEmpty())) {

                Join<CityRegion, City> cityJoin = root.join("location").join("cityRegion").join("city");
                predicates.add(cityJoin.get("id").in(pageRequestDto.getFilters().getLocation().getId()));
            }
        }

        if (pageRequestDto.getFilters().getMicroLocation() != null && !pageRequestDto.getFilters().getMicroLocation().isEmpty()) {
            List<Long> citySubregionIds = pageRequestDto.getFilters().getMicroLocation().stream()
                    .map(CitySubregionDto::getId)
                    .collect(Collectors.toList());

            Join<CitySubregion, City> cityRegionJoin = root.join("location");
            predicates.add(cityRegionJoin.get("id").in(citySubregionIds));
        }

        if (pageRequestDto.getFilters().getPriceLess() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), pageRequestDto.getFilters().getPriceLess()));
        }
        if (pageRequestDto.getFilters().getPriceHigher() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), pageRequestDto.getFilters().getPriceHigher()));
        }
        if (pageRequestDto.getFilters().getSpaceAreaLess() != null) {
            predicates.add(criteriaBuilder.lessThan(root.get("livingSpaceArea"), pageRequestDto.getFilters().getSpaceAreaLess()));
        }
        if (pageRequestDto.getFilters().getSpaceAreaHigher() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("livingSpaceArea"), pageRequestDto.getFilters().getSpaceAreaHigher()));
        }

        if (pageRequestDto.getFilters().getRoomsNumberHigher() != null) {
            String minRoomsNumber = EnumUtils.fromLabel(RoomsNumber.class, pageRequestDto.getFilters().getRoomsNumberHigher()).toString();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("roomsNumber").as(String.class), minRoomsNumber));
        }

        if (pageRequestDto.getFilters().getRoomsNumberLess() != null) {
            String maxRoomsNumber = EnumUtils.fromLabel(RoomsNumber.class, pageRequestDto.getFilters().getRoomsNumberLess()).toString();
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("roomsNumber").as(String.class), maxRoomsNumber));
        }

        if (pageRequestDto.getFilters().getAdType() != null && !pageRequestDto.getFilters().getAdType().isEmpty()) {
            List<AdType> desiredAdTypes = pageRequestDto.getFilters().getAdType().stream()
                    .map(label -> EnumUtils.fromLabel(AdType.class, label))
                    .collect(Collectors.toList());
            Expression<AdType> adTypeExpression = root.get("adType");
            predicates.add(adTypeExpression.in(desiredAdTypes));
        }

        if (pageRequestDto.getFilters().getType() != null && !pageRequestDto.getFilters().getType().isEmpty()) {
            List<RealEstateType> desiredTypes = pageRequestDto.getFilters().getType().stream()
                    .map(label -> EnumUtils.fromLabel(RealEstateType.class, label))
                    .collect(Collectors.toList());
            Expression<RealEstateType> typeExpression = root.get("realEstateType");
            predicates.add(typeExpression.in(desiredTypes));
        }

        if (pageRequestDto.getFilters().getHeatingType() != null && !pageRequestDto.getFilters().getHeatingType().isEmpty()) {
            List<HeatingType> desiredHeatingTypes = pageRequestDto.getFilters().getHeatingType().stream()
                    .map(label -> EnumUtils.fromLabel(HeatingType.class, label))
                    .collect(Collectors.toList());
            Expression<HeatingType> heatingTypeExpression = root.get("heatingType");
            predicates.add(heatingTypeExpression.in(desiredHeatingTypes));
        }

        if (pageRequestDto.getFilters().getFurniture() != null && !pageRequestDto.getFilters().getFurniture().isEmpty()) {
            List<FurnitureType> desiredFurniture = pageRequestDto.getFilters().getFurniture().stream()
                    .map(label -> EnumUtils.fromLabel(FurnitureType.class, label))
                    .collect(Collectors.toList());
            Expression<FurnitureType> furnitureTypeExpression = root.get("furnitureType");
            predicates.add(furnitureTypeExpression.in(desiredFurniture));
        }

        if (pageRequestDto.getFilters().getFloor() != null && !pageRequestDto.getFilters().getFloor().isEmpty()) {
            List<Floor> desiredFloors = pageRequestDto.getFilters().getFloor().stream()
                    .map(label -> EnumUtils.fromLabel(Floor.class, label))
                    .collect(Collectors.toList());
            Expression<Floor> floorExpression = root.get("floor");
            predicates.add(floorExpression.in(desiredFloors));
        }

        return predicates;
    }

}
