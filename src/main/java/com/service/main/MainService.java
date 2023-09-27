package com.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dto.responseDto.ApiDataResponseDto;
import com.dto.resquestDto.CurrencyConversion;

public interface MainService {

    public ApiDataResponseDto getCountryInfo(String country) throws JsonProcessingException;

    public ApiDataResponseDto getStateAndCities(String country);

    public ApiDataResponseDto currencyConversion(CurrencyConversion currencyConversion);
}
