package com.unit;

import com.dto.responseDto.population.CountryPopulationCount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.config.http.HttpClient;
import com.constant.URIConstant;
import com.dto.responseDto.HttpBaseResponse;
import com.dto.responseDto.population.FilterPopulationResponse;
import com.dto.responseDto.population.GetCountryPopulation;
import com.dto.responseDto.population.PopulationCount;
import com.dto.resquestDto.FilterCountry;
import com.dto.resquestDto.population.FilterPopulation;
import com.service.population.PopulationServiceImpl;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PopulationTest {

    @InjectMocks
    private PopulationServiceImpl populationService;

    @Mock
    private HttpClient httpClient;

    HttpBaseResponse<GetCountryPopulation> httpBaseResponse;


    @Value("${base-url}")
    private String baseUrl;

    @BeforeEach
    public void setup(){


        CountryPopulationCount countryPopulationCount1 = new CountryPopulationCount();
        countryPopulationCount1.setValue(45138458);
        countryPopulationCount1.setYear(1960);

        CountryPopulationCount countryPopulationCount2 = new CountryPopulationCount();
        countryPopulationCount2.setValue(46063563);
        countryPopulationCount2.setYear(1961);

        List<CountryPopulationCount> countryPopulationCounts = new ArrayList<>(List.of(countryPopulationCount1, countryPopulationCount2));

        GetCountryPopulation getCountryPopulation = new GetCountryPopulation();
        getCountryPopulation.setCountry("Nigeria");
        getCountryPopulation.setCode("NGA");
        getCountryPopulation.setIso3("NGA");
        getCountryPopulation.setPopulationCounts(countryPopulationCounts);

        httpBaseResponse = new HttpBaseResponse<>();
        httpBaseResponse.setError(false);
        httpBaseResponse.setMsg("Nigeria with population");
        httpBaseResponse.setData(getCountryPopulation);

    }

    @Test
    public void getCurrentPopulationTest() throws IOException {

        final String url = baseUrl + URIConstant.POPULATION_COUNTRY_FILTER;

        FilterCountry filterCountry = new FilterCountry();
        filterCountry.setCountry("Nigeria");

        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://example.com")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .header("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), String.valueOf(httpBaseResponse)))
                .build();

        when(httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)).thenReturn(response);


        when(httpClient.toPojo(any(),  any(TypeReference.class))).thenReturn(httpBaseResponse);

        int population = populationService.getCurrentPopulation(filterCountry);

        assertThat(population).isEqualTo(46063563);
    }

    @Test
    public void getMostPopulatedCitiesTest() throws IOException {

        final String url =  baseUrl + URIConstant.POPULATION_CITIES_FILTER;

        FilterPopulation filterPopulation = new FilterPopulation();
        filterPopulation.setLimit(2);
        filterPopulation.setOrder("dsc");
        filterPopulation.setOrderBy("value");
        filterPopulation.setCountry("Nigeria");

        PopulationCount populationCount = new PopulationCount();
        populationCount.setValue("2222");
        populationCount.setYear("2020");
        populationCount.setReliabilty("Final figure, complete");
        populationCount.setSex("Both Sexes");

        List<PopulationCount> populationCounts = new ArrayList<>(List.of(populationCount));

        FilterPopulationResponse filterPopulationResponse1 = new FilterPopulationResponse();
        filterPopulationResponse1.setPopulationCounts(populationCounts);
        filterPopulationResponse1.setCity("lagos");
        filterPopulationResponse1.setCountry("nigeria");

        FilterPopulationResponse filterPopulationResponse2 = new FilterPopulationResponse();
        filterPopulationResponse2.setPopulationCounts(populationCounts);
        filterPopulationResponse2.setCity("abuja");
        filterPopulationResponse2.setCountry("nigeria");

        List<FilterPopulationResponse> filterPopulationResponses = new ArrayList<>(List.of(filterPopulationResponse1, filterPopulationResponse2));

        HttpBaseResponse<List<FilterPopulationResponse>> httpBaseResponse = new HttpBaseResponse<>();
        httpBaseResponse.setError(false);
        httpBaseResponse.setMsg("filtered result");
        httpBaseResponse.setData(filterPopulationResponses);

        Response response = new Response.Builder()
                .request(new Request.Builder()
                        .url("https://example.com")
                        .build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .header("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), String.valueOf(httpBaseResponse)))
                .build();

        when(httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterPopulation), ""), filterPopulation,
                url)).thenReturn(response);


        when(httpClient.toPojo(any(),  any(TypeReference.class))).thenReturn(httpBaseResponse);

        List<String> population = populationService.getMostPopulatedCities(2,"Nigeria");

        assertThat(population.size()).isEqualTo(2);
    }

}
