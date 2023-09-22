package com.klasha.service.capital;

import com.fasterxml.jackson.core.type.TypeReference;
import com.klasha.config.http.KlashaHttpClient;
import com.klasha.constant.URIConstant;
import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.responseDto.capital.CapitalResponse;
import com.klasha.dto.responseDto.population.CountryPopulationCount;
import com.klasha.dto.responseDto.population.GetCountryPopulation;
import com.klasha.dto.resquestDto.FilterCountry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CapitalService {

    private final KlashaHttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public CapitalResponse getCapitalCity(FilterCountry filterCountry){
        BaseResponse<CapitalResponse> capitalCity = getCountryCapital(filterCountry);
        assert capitalCity != null;
        return capitalCity.getData();
    }

    @Nullable
    public BaseResponse<CapitalResponse> getCountryCapital(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.COUNTRY_CAPITAL_FILTER;
        try (Response response = httpClient.postFormParam(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<BaseResponse<CapitalResponse>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}