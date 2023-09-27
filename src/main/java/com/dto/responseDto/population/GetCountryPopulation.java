package com.dto.responseDto.population;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCountryPopulation {
    private String country;
    private String code;
    private String iso3;
    private List<CountryPopulationCount> populationCounts;
}
