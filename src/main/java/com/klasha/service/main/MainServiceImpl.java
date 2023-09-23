package com.klasha.service.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.State;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Map<String, String> getCountryInfo(String country) throws JsonProcessingException {
        return new HashMap<String, String>() {{
            put("population", String.valueOf(populationServiceImpl.getCurrentPopulation(getFilterCountry(country))));
            put("capital city", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getCapital());
            put("location", locationServiceImpl.getLocation(getFilterCountry(country)));
            put("currency", currencyServiceImpl.getCurrency(getFilterCountry(country)));
            put("ISO2", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getIso2());
            put("ISO3", capitalServiceImpl.getCapitalCity(getFilterCountry(country)).getIso3());
        }};
    }

    public List<StateCitiesResponse> getStateAndCities(String country) {
        List<StateCitiesResponse> stateCitiesResponses = new ArrayList<>();
        List<State> states = Objects.requireNonNull(stateServiceImpl.getStates(getFilterCountry(country))).getData().getStates();
        for (State state : states) {
            StateCitiesResponse stateCitiesResponse = new StateCitiesResponse();
            List<String> cities = Objects.requireNonNull(cityServiceImpl.getCities(new CityRequest(country, state.getName()))).getData();
            stateCitiesResponse.setState(state.getName());
            stateCitiesResponse.setCities(cities);
            stateCitiesResponses.add(stateCitiesResponse);
        }
        if (stateCitiesResponses.isEmpty()) throw new BadRequestException("Failed to fetch");
        return stateCitiesResponses;
    }

    public CurrencyConversionResponse currencyConversion(CurrencyConversion currencyConversion) {
        String sourceCurrency = currencyServiceImpl.getCurrency(getFilterCountry(currencyConversion.getCountry()));
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
