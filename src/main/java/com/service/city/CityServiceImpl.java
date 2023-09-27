package com.service.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.config.http.HttpClient;
import com.constant.URIConstant;
import com.dto.responseDto.HttpBaseResponse;
import com.dto.resquestDto.city.CityRequest;
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
public class CityServiceImpl implements CityService {

    private final HttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    @Nullable
    public HttpBaseResponse<List<String>> getCities(CityRequest cityRequest) {
        final String url =  baseUrl + URIConstant.COUNTRY_STATE_CITIES;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(cityRequest), ""),
                cityRequest,
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<List<String>>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
