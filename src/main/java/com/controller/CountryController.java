package com.controller;


import com.dto.responseDto.ApiDataResponseDto;
import com.dto.resquestDto.CurrencyConversion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.main.MainService;
import com.service.population.PopulationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final PopulationService populationService;

    private final MainService mainService;



    @GetMapping("/country/cities")
    public ResponseEntity<ApiDataResponseDto> getCountryCities(@RequestParam(name = "limit") int numberOfCities) {
        return ResponseEntity.ok(populationService.getCountryCities(numberOfCities));
    }

    @GetMapping("/country/info")
    public ResponseEntity<ApiDataResponseDto> getCountryInfo(@RequestParam String country) throws JsonProcessingException {
        return ResponseEntity.ok(mainService.getCountryInfo(country));
    }

    @GetMapping("/country/state/cities")
    public ResponseEntity<ApiDataResponseDto> getStateAndCities(@RequestParam String country) {
        return ResponseEntity.ok(mainService.getStateAndCities(country));
    }

    @PostMapping("/country/currency/conversion")
    public ResponseEntity<ApiDataResponseDto> currencyConversion(@Valid @RequestBody CurrencyConversion currencyConversion) {
        return ResponseEntity.ok(mainService.currencyConversion(currencyConversion));
    }

}
