package com.klasha.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.klasha.dto.responseDto.population.PopulationCount;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityResponseDto {
    private String city;
    private String country;
    private List<PopulationCount> populationCounts;
}
