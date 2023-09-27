package com.dto.responseDto.population;

import lombok.Data;

import java.util.List;

@Data
public class FilterPopulationResponse {
    private String city;
    private String country;
    private List<PopulationCount> populationCounts;
}