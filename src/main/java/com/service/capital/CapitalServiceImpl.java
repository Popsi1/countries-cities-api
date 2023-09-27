package com.service.capital;

import com.dto.responseDto.capital.CapitalResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.config.http.HttpClient;
import com.constant.URIConstant;
import com.dto.responseDto.HttpBaseResponse;
import com.dto.resquestDto.FilterCountry;
import com.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CapitalServiceImpl implements CapitalService {

    private final HttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public CapitalResponse getCapitalCity(FilterCountry filterCountry){
        HttpBaseResponse<CapitalResponse> capitalCity = getCountryCapital(filterCountry);
        assert capitalCity != null;
        if (capitalCity.isError() || ObjectUtils.isEmpty(capitalCity.getData())) {
            log.error("get capital error message :: {}", capitalCity.getMsg());
            throw new BadRequestException("Failed to fetch " + filterCountry.getCountry() + " capital");
        }

        return capitalCity.getData();
    }

    @Nullable
    public HttpBaseResponse<CapitalResponse> getCountryCapital(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.COUNTRY_CAPITAL_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<CapitalResponse>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
