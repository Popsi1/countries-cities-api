package com.service.currency;

import com.dto.responseDto.currency.CurrencyResponse;
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
public class CurrencyServiceImpl implements CurrencyService {

    private final HttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public String getCurrency(FilterCountry filterCountry){
        HttpBaseResponse<CurrencyResponse> currency = getCountryCurrency(filterCountry);
        assert currency != null;
        if (currency.isError() || ObjectUtils.isEmpty(currency.getData())) {
            log.error("get currency error message :: {}", currency.getMsg());
            throw new BadRequestException("Failed to fetch " + filterCountry.getCountry() + " currency");
        }

        return currency.getData().getCurrency();
    }

    @Nullable
    public HttpBaseResponse<CurrencyResponse> getCountryCurrency(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.COUNTRY_CURRENCY_FILTER;
        try (Response response = httpClient.postForm(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<HttpBaseResponse<CurrencyResponse>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
