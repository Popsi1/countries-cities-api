package com.klasha.service.population;

import com.klasha.dto.responseDto.ApiDataResponseDto;
import com.klasha.dto.resquestDto.FilterCountry;

import java.util.List;
import java.util.Map;

public interface PopulationService {
    public ApiDataResponseDto getCountryCities(int numberOfCities);
    public int getCurrentPopulation(FilterCountry filterCountry);
}
