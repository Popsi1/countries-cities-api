package com.service.location;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class LocationServiceImpl implements LocationService {

    private final HttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public String getLocation(FilterCountry filterCountry) throws JsonProcessingException {
        HttpBaseResponse<Object> countryLocation = getCountryLocation(filterCountry);
        assert countryLocation != null;
        if (countryLocation.isError() || ObjectUtils.isEmpty(countryLocation.getData())){
            log.error("get country location error message :: {}", countryLocation.getMsg());
            throw new BadRequestException("Failed to fetch " + filterCountry.getCountry() + " location");
        }

        return parseLocation(countryLocation.getData());
    }

    public String parseLocation(Object location) throws JsonProcessingException {
        String[] fields = location.toString().split(",");
        String[] lat = fields[3].split("}");
        return fields[2].trim() + "," + lat[0].trim();

    }

    @Nullable
    public HttpBaseResponse<Object> getCountryLocation(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.LOCATION_COUNTRY_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<Object>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
