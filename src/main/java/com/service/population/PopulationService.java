package com.service.population;

import com.dto.responseDto.ApiDataResponseDto;
import com.dto.resquestDto.FilterCountry;

public interface PopulationService {
    public ApiDataResponseDto getCountryCities(int numberOfCities);
    public int getCurrentPopulation(FilterCountry filterCountry);
}
