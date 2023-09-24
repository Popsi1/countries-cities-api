package com.klasha.service.state;

import com.fasterxml.jackson.core.type.TypeReference;
import com.klasha.config.http.KlashaHttpClient;
import com.klasha.constant.URIConstant;
import com.klasha.dto.responseDto.HttpBaseResponse;
import com.klasha.dto.resquestDto.FilterCountry;
import com.klasha.dto.responseDto.state.StateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

    private final KlashaHttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    @Nullable
    public HttpBaseResponse<StateResponse> getStates(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.COUNTRY_STATE;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<StateResponse>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
