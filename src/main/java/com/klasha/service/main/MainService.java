package com.klasha.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.State;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;
import com.klasha.dto.resquestDto.FilterCountry;
import com.klasha.dto.resquestDto.city.CityRequest;
import com.klasha.enums.ExchangeRate;
import com.klasha.service.capital.CapitalService;
import com.klasha.service.city.CityService;
import com.klasha.service.currency.CurrencyService;
import com.klasha.service.location.LocationService;
import com.klasha.service.population.PopulationService;
import com.klasha.service.state.StateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final PopulationService populationService;
    private final CapitalService capitalService;
    private final CurrencyService currencyService;

    private final CityService cityService;
    private final StateService stateService;
    private final LocationService locationService;

    public Map<String, String> getCountryInfo(String country) throws JsonProcessingException {
        return new HashMap<String, String>() {{
            put("population", String.valueOf(populationService.getCurrentPopulation(getFilterCountry(country))));
            put("capital city", capitalService.getCapitalCity(getFilterCountry(country)).getCapital());
            put("location", locationService.getLocation(getFilterCountry(country)));
            put("currency", currencyService.getCurrency(getFilterCountry(country)));
            put("ISO2", capitalService.getCapitalCity(getFilterCountry(country)).getIso2());
            put("ISO3", capitalService.getCapitalCity(getFilterCountry(country)).getIso3());
        }};
    }

    public List<StateCitiesResponse> getStateAndCities(String country) {
        List<StateCitiesResponse> stateCitiesResponses = new ArrayList<>();
        List<State> states = Objects.requireNonNull(stateService.getStates(getFilterCountry(country))).getData().getStates();
        for (State state : states) {
            StateCitiesResponse stateCitiesResponse = new StateCitiesResponse();
            List<String> cities = Objects.requireNonNull(cityService.getCities(new CityRequest(country, state.getName()))).getData();
            stateCitiesResponse.setState(state.getName());
            stateCitiesResponse.setCities(cities);
            stateCitiesResponses.add(stateCitiesResponse);
        }
        return stateCitiesResponses;
    }

    public CurrencyConversionResponse currencyConversion(CurrencyConversion currencyConversion) {
        String sourceCurrency = currencyService.getCurrency(getFilterCountry(currencyConversion.getCountry()));
        double rate = ExchangeRate.getRate(sourceCurrency, currencyConversion.getTargetCurrency());
        double amountConverted = currencyConversion.getAmount() * rate;
        return new CurrencyConversionResponse(currencyConversion.getTargetCurrency(), amountConverted);
    }

    public FilterCountry getFilterCountry(String country){
        FilterCountry filterCountry = new FilterCountry();
        filterCountry.setCountry(country);
        return filterCountry;
    }
}
