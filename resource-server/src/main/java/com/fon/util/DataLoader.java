package com.fon.util;

import com.fon.DAO.CityRegionRepository;
import com.fon.DAO.CityRepository;
import com.fon.DAO.CitySubregionRepository;
import com.fon.entity.City;
import com.fon.entity.CityRegion;
import com.fon.entity.CitySubregion;
import com.fon.entity.GeoLocation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DataLoader {

    private final CityRepository cityRepository;
    private final CityRegionRepository cityRegionRepository;
    private final CitySubregionRepository citySubregionRepository;

    @Autowired
    public DataLoader(CityRepository cityRepository, CityRegionRepository cityRegionRepository,
                      CitySubregionRepository citySubregionRepository) {
        this.cityRepository = cityRepository;
        this.cityRegionRepository = cityRegionRepository;
        this.citySubregionRepository = citySubregionRepository;
    }

    @PostConstruct
    public void loadData() {
        // Check if the City table is empty
        if (cityRepository.count() == 0) {
            System.out.println("Loading cities...");
            List<City> cities = loadCitiesFromCSV();
            cityRepository.saveAll(cities);
            System.out.println("Cities loaded...");
        }

        if (cityRegionRepository.count() == 0) {
            System.out.println("Loading city regions...");
            List<CityRegion> cityRegions = loadCityRegionsFromCSV();
            cityRegionRepository.saveAll(cityRegions);
            System.out.println("City regions loaded...");
        }

        if (citySubregionRepository.count() == 0) {
            System.out.println("Loading city sub regions...");
            List<CitySubregion> citySubregions = loadCitySubregionsFromCSV();
            citySubregionRepository.saveAll(citySubregions);
            System.out.println("City sub regions loaded...");
        }

    }

    private List<CitySubregion> loadCitySubregionsFromCSV() {
        List<CitySubregion> cityRegions = new ArrayList<>();
        Long id = null;
        try (CSVParser parser = new CSVParser(new FileReader(Objects.requireNonNull(getClass().getResource("/db-csv/city-subregions-data.csv")).getPath()), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {
                id = Long.parseLong(record.get(0));
                cityRegions.add(CitySubregion.builder()
                        .id(Long.parseLong(record.get(0)))
                        .name(record.get(1))
                        .geoLocation(new GeoLocation(Double.parseDouble(record.get(2)), Double.parseDouble(record.get(3))))
                        .zoom(Integer.parseInt(record.get(4)))
                        .order(Long.parseLong(record.get(5)))
                        .cityRegion(cityRegionRepository.findById(Long.parseLong(record.get(6))).get())
                        .build());
            }
        } catch (Exception e) {
            System.out.println(id);
            e.printStackTrace();
        }

        return cityRegions;
    }

    private List<CityRegion> loadCityRegionsFromCSV() {
        List<CityRegion> cityRegions = new ArrayList<>();

        try (CSVParser parser = new CSVParser(new FileReader(Objects.requireNonNull(getClass().getResource("/db-csv/city-regions-data.csv")).getPath()), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {

                cityRegions.add(CityRegion.builder()
                        .id(Long.parseLong(record.get(0)))
                        .name(record.get(1))
                        .geoLocation(new GeoLocation(Double.parseDouble(record.get(2)), Double.parseDouble(record.get(3))))
                        .zoom(Integer.parseInt(record.get(4)))
                        .order(Long.parseLong(record.get(5)))
                        .city(cityRepository.findById(Long.parseLong(record.get(6))).get())
                        .build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityRegions;
    }

    private List<City> loadCitiesFromCSV() {
        List<City> cities = new ArrayList<>();

        try (CSVParser parser = new CSVParser(new FileReader(Objects.requireNonNull(getClass().getResource("/db-csv/city-data.csv")).getPath()), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {

                cities.add(City.builder()
                        .id(Long.parseLong(record.get(0)))
                        .name(record.get(1))
                        .geoLocation(new GeoLocation(Double.parseDouble(record.get(2)), Double.parseDouble(record.get(3))))
                        .zoom(Integer.parseInt(record.get(4)))
                        .order(Long.parseLong(record.get(5)))
                        .build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cities;
    }
}