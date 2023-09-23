package com.klasha.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;

import java.util.List;
import java.util.Map;

public interface MainService {

    public Map<String, String> getCountryInfo(String country) throws JsonProcessingException;

    public List<StateCitiesResponse> getStateAndCities(String country);

    public CurrencyConversionResponse currencyConversion(CurrencyConversion currencyConversion);
}
