package com.klasha.service.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.klasha.config.http.KlashaHttpClient;
import com.klasha.constant.URIConstant;
import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.resquestDto.city.CityRequest;
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
public class CityService {

    private final KlashaHttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    @Nullable
    public BaseResponse<List<String>> getCities(CityRequest cityRequest) {
        final String url =  baseUrl + URIConstant.COUNTRY_STATE_CITIES;
        try (Response response = httpClient.postFormParam(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(cityRequest), ""),
                cityRequest,
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<BaseResponse<List<String>>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
