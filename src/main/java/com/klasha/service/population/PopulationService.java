package com.klasha.service.population;

import com.fasterxml.jackson.core.type.TypeReference;
import com.klasha.config.http.KlashaHttpClient;
import com.klasha.constant.URIConstant;
import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.responseDto.population.CountryPopulationCount;
import com.klasha.dto.resquestDto.FilterCountry;
import com.klasha.dto.responseDto.population.FilterPopulationResponse;
import com.klasha.dto.responseDto.population.GetCountryPopulation;
import com.klasha.dto.resquestDto.population.FilterPopulation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PopulationService {

    private final KlashaHttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public Map<String, List<String>> getCountryCities(int numberOfCities){

        return new HashMap<String, List<String>>() {{
            put("Italy", italyMostPopulatedCities(numberOfCities));
            put("New Zealand", newZealandMostPopulatedCities(numberOfCities));
            put("Ghana", ghanaMostPopulatedCities(numberOfCities));
        }};
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

    public List<String> getMostPopulatedCities(int numberOfCities, String country){
        List<String> cities = new ArrayList<>();

        FilterPopulation filterPopulation = new FilterPopulation();
        filterPopulation.setLimit(numberOfCities);
        filterPopulation.setOrder("dsc");
        filterPopulation.setOrderBy("value");
        filterPopulation.setCountry(country);

        BaseResponse<List<FilterPopulationResponse>> filterPopulations = filterPopulation(filterPopulation);

        assert filterPopulations != null;
        for (FilterPopulationResponse filterPopulationResponse : filterPopulations.getData()) {
            cities.add(filterPopulationResponse.getCity());
        }

        return cities;
    }

    @Nullable
    public BaseResponse<List<FilterPopulationResponse>> filterPopulation(FilterPopulation filterPopulation) {
        final String url =  baseUrl + URIConstant.POPULATION_CITIES_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterPopulation), ""), filterPopulation,
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<BaseResponse<List<FilterPopulationResponse>>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }

    public int getCurrentPopulation(FilterCountry filterCountry){
        BaseResponse<GetCountryPopulation> populations = getCountryPopulation(filterCountry);
        assert populations != null;
        List<CountryPopulationCount> populationCounts = populations.getData().getPopulationCounts();
        return populationCounts.get(populationCounts.size() - 1).getValue();
    }

    @Nullable
    public BaseResponse<GetCountryPopulation> getCountryPopulation(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.POPULATION_COUNTRY_FILTER;
        try (Response response = httpClient.postFormParam(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<BaseResponse<GetCountryPopulation>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
