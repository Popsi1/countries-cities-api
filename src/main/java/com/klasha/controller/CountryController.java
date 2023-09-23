package com.klasha.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.responseDto.CurrencyConversionResponse;
import com.klasha.dto.responseDto.state.StateCitiesResponse;
import com.klasha.dto.resquestDto.CurrencyConversion;
import com.klasha.service.main.MainServiceImpl;
import com.klasha.service.population.PopulationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final PopulationServiceImpl populationServiceImpl;

    private final MainServiceImpl mainServiceImpl;



    @GetMapping("/country/cities")
    public ResponseEntity<Map<String, List<String>>> getCountryCities(@RequestParam(name = "limit") int numberOfCities) {
        return ResponseEntity.ok(populationServiceImpl.getCountryCities(numberOfCities));
    }

    @GetMapping("/country/info")
    public ResponseEntity<Map<String, String>> getCountryInfo(@RequestParam String country) throws JsonProcessingException {
        return ResponseEntity.ok(mainServiceImpl.getCountryInfo(country));
    }

    @GetMapping("/country/state/cities")
    public ResponseEntity<List<StateCitiesResponse>> getStateAndCities(@RequestParam String country) {
        return ResponseEntity.ok(mainServiceImpl.getStateAndCities(country));
    }

    @PostMapping("/country/currency/conversion")
    public ResponseEntity<CurrencyConversionResponse> currencyConversion(@Valid @RequestBody CurrencyConversion currencyConversion) {
        return ResponseEntity.ok(mainServiceImpl.currencyConversion(currencyConversion));
    }


}
