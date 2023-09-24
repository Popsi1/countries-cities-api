package com.klasha.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.ApiDataResponseDto;
import com.klasha.dto.responseDto.currency.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;

import java.util.List;
import java.util.Map;

public interface MainService {

    public ApiDataResponseDto getCountryInfo(String country) throws JsonProcessingException;

    public ApiDataResponseDto getStateAndCities(String country);

    public ApiDataResponseDto currencyConversion(CurrencyConversion currencyConversion);
}
