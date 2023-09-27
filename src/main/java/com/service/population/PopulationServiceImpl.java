package com.service.population;

import com.dto.responseDto.ApiDataResponseDto;
import com.dto.responseDto.population.CountryPopulationCount;
import com.dto.responseDto.population.FilterPopulationResponse;
import com.dto.resquestDto.population.FilterPopulation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.config.http.HttpClient;
import com.constant.URIConstant;
import com.dto.responseDto.HttpBaseResponse;
import com.dto.resquestDto.FilterCountry;
import com.dto.responseDto.population.GetCountryPopulation;
import com.exception.BadRequestException;
import com.utils.DataResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopulationServiceImpl implements PopulationService {

    private final HttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public ApiDataResponseDto getCountryCities(int numberOfCities) {

        ConcurrentHashMap<String, List<String>> result = new ConcurrentHashMap<String, List<String>>() {{
            put("Italy", italyMostPopulatedCities(numberOfCities));
            put("New Zealand", newZealandMostPopulatedCities(numberOfCities));
            put("Ghana", ghanaMostPopulatedCities(numberOfCities));
        }};

        return DataResponseUtils.successResponse("Cities successfully retrieved", result);
    }

    public List<String> italyMostPopulatedCities(int numberOfCities){
        return getMostPopulatedCities(numberOfCities, "Italy");
    }

    public List<String> newZealandMostPopulatedCities(int numberOfCities){
        return getMostPopulatedCities(numberOfCities, "New Zealand");
    }

    public List<String> ghanaMostPopulatedCities(int numberOfCities){
        return getMostPopulatedCities(numberOfCities, "Ghana");
    }

    public List<String> getMostPopulatedCities(int numberOfCities, String country) {
        List<String> cities = new ArrayList<>();

        FilterPopulation filterPopulation = new FilterPopulation();
        filterPopulation.setLimit(numberOfCities);
        filterPopulation.setOrder("dsc");
        filterPopulation.setOrderBy("value");
        filterPopulation.setCountry(country);

        HttpBaseResponse<List<FilterPopulationResponse>> filterPopulations = filterPopulation(filterPopulation);
        assert filterPopulations != null;
        if (filterPopulations.isError() || ObjectUtils.isEmpty(filterPopulations.getData())){
            log.error("filter population error message :: {}", filterPopulations.getMsg());
            throw new BadRequestException("Failed to fetch " + country + " most populated cities");
        }

        for (FilterPopulationResponse filterPopulationResponse : filterPopulations.getData()) {
            cities.add(filterPopulationResponse.getCity());
        }

        return cities;
    }

    @Nullable
    public HttpBaseResponse<List<FilterPopulationResponse>> filterPopulation(FilterPopulation filterPopulation) {
        final String url =  baseUrl + URIConstant.POPULATION_CITIES_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterPopulation), ""), filterPopulation,
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<List<FilterPopulationResponse>>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }

    public int getCurrentPopulation(FilterCountry filterCountry){
        HttpBaseResponse<GetCountryPopulation> populations = getCountryPopulation(filterCountry);
        assert populations != null;
        if (populations.isError() || ObjectUtils.isEmpty(populations.getData())) {
            log.error("get country population error message :: {}", populations.getMsg());
            throw new BadRequestException("Failed to fetch " + filterCountry.getCountry() + " current population");
        }

        List<CountryPopulationCount> populationCounts = populations.getData().getPopulationCounts();
        return populationCounts.get(populationCounts.size() - 1).getValue();
    }

    @Nullable
    public HttpBaseResponse<GetCountryPopulation> getCountryPopulation(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.POPULATION_COUNTRY_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<GetCountryPopulation>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
