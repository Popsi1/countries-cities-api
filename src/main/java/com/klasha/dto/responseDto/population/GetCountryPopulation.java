package com.klasha.dto.responseDto.population;

import lombok.Data;

import java.util.List;
@Data
public class GetCountryPopulation {
    private String country;
    private String code;
    private String iso3;
    private List<CountryPopulationCount> populationCounts;
}
