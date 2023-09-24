package com.klasha.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.ApiDataResponseDto;
import com.klasha.dto.responseDto.HttpBaseResponse;
import com.klasha.dto.responseDto.currency.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.State;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
import com.klasha.dto.responseDto.state.StateResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;
import com.klasha.dto.resquestDto.FilterCountry;
import com.klasha.dto.resquestDto.city.CityRequest;
import com.klasha.enums.ExchangeRate;
import com.klasha.exception.BadRequestException;
import com.klasha.service.capital.CapitalServiceImpl;
import com.klasha.service.city.CityServiceImpl;
import com.klasha.service.currency.CurrencyServiceImpl;
import com.klasha.service.location.LocationServiceImpl;
import com.klasha.service.population.PopulationServiceImpl;
import com.klasha.service.state.StateServiceImpl;
import com.klasha.utils.DataResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final PopulationServiceImpl populationServiceImpl;
    private final CapitalServiceImpl capitalServiceImpl;
    private final CurrencyServiceImpl currencyServiceImpl;

    private final CityServiceImpl cityServiceImpl;
    private final StateServiceImpl stateServiceImpl;
    private final LocationServiceImpl locationServiceImpl;

    public ApiDataResponseDto getCountryInfo(String country) throws JsonProcessingException {
        ConcurrentHashMap<String, String> result = new ConcurrentHashMap<String, String>() {{
            put("population", String.valueOf(populationServiceImpl.getCurrentPopulation(getFilterCountry(country))));
            put("capital city", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getCapital());
            put("location", locationServiceImpl.getLocation(getFilterCountry(country)));
            put("currency", currencyServiceImpl.getCurrency(getFilterCountry(country)));
            put("ISO2", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getIso2());
            put("ISO3", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getIso3());
        }};

        return DataResponseUtils.successResponse(country.concat(" info successfully retrieved"), result);
    }

    public ApiDataResponseDto getStateAndCities(String country) {
        List<StateCitiesResponse> stateCitiesResponses = new ArrayList<>();

        HttpBaseResponse<StateResponse> states = stateServiceImpl.getStates(getFilterCountry(country));
        assert states != null;
        if (states.isError() || ObjectUtils.isEmpty(states.getData())) {
            log.error("get states error message :: {}", states.getMsg());
            throw new BadRequestException("Failed to fetch " + country + " states");
        }

        for (State state : states.getData().getStates()) {
            StateCitiesResponse stateCitiesResponse = new StateCitiesResponse();
            List<String> cities = Objects.requireNonNull(cityServiceImpl.getCities(new CityRequest(country, state.getName()))).getData();
            stateCitiesResponse.setState(state.getName());
            stateCitiesResponse.setCities(cities);
            stateCitiesResponses.add(stateCitiesResponse);
        }

        if (stateCitiesResponses.isEmpty()) throw new BadRequestException("Failed to fetch " + country + " state cities");

        return DataResponseUtils.successResponse(country.concat(" state cities successfully retrieved"), stateCitiesResponses);
    }

    public ApiDataResponseDto currencyConversion(CurrencyConversion currencyConversion) {
        String sourceCurrency = currencyServiceImpl.getCurrency(getFilterCountry(currencyConversion.getCountry()));
        double rate = ExchangeRate.getRate(sourceCurrency, currencyConversion.getTargetCurrency());
        double amountConverted = currencyConversion.getAmount() * rate;
        return DataResponseUtils.successResponse("Currency conversion successful",
                new CurrencyConversionResponse(currencyConversion.getTargetCurrency(), amountConverted));
    }

    public FilterCountry getFilterCountry(String country){
        FilterCountry filterCountry = new FilterCountry();
        filterCountry.setCountry(country);
        return filterCountry;
    }
}
