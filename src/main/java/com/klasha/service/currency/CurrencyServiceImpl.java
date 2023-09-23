package com.klasha.service.currency;

import com.fasterxml.jackson.core.type.TypeReference;
import com.klasha.config.http.KlashaHttpClient;
import com.klasha.constant.URIConstant;
import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.responseDto.currency.CurrencyResponse;
import com.klasha.dto.resquestDto.FilterCountry;
import com.klasha.exception.BadRequestException;
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

    private final KlashaHttpClient httpClient;

    @Value("${base-url}")
    private String baseUrl;

    public String getCurrency(FilterCountry filterCountry){
        BaseResponse<CurrencyResponse> currency = getCountryCurrency(filterCountry);
        assert currency != null;
        if (ObjectUtils.isEmpty(currency.getData())) throw new BadRequestException("Failed to fetch");
        return currency.getData().getCurrency();
    }

    @Nullable
    public BaseResponse<CurrencyResponse> getCountryCurrency(FilterCountry filterCountry) {
        final String url =  baseUrl + URIConstant.COUNTRY_CURRENCY_FILTER;
        try (Response response = httpClient.postFormParam(
                Collections.singletonMap("ContentType", "application/x-www-form-urlencoded"),
                Collections.singletonMap(httpClient.toJson(filterCountry), ""),
                filterCountry.getCountry(),
                url)) {
            assert response.body() != null;
            final String json = response.body().string();
            log.info("--> Response :: {}", json);
            return httpClient.toPojo(json, new TypeReference<BaseResponse<CurrencyResponse>>() {
            });
        } catch (Exception e) {
            log.error("Remote exception :: {}", e.getMessage());
            return null;
        }
    }
}
