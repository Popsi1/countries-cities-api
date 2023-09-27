package com.service.main;

import com.dto.responseDto.ApiDataResponseDto;
import com.dto.resquestDto.city.CityRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dto.responseDto.HttpBaseResponse;
import com.dto.responseDto.currency.CurrencyConversionResponse;
import com.dto.responseDto.state.State;
import com.dto.responseDto.state.StateCitiesResponse;
import com.dto.responseDto.state.StateResponse;
import com.dto.resquestDto.CurrencyConversion;
import com.dto.resquestDto.FilterCountry;
import com.enums.ExchangeRate;
import com.exception.BadRequestException;
import com.service.capital.CapitalServiceImpl;
import com.service.city.CityServiceImpl;
import com.service.currency.CurrencyServiceImpl;
import com.service.location.LocationServiceImpl;
import com.service.population.PopulationServiceImpl;
import com.service.state.StateServiceImpl;
import com.utils.DataResponseUtils;
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
