package com.config.http;

import com.dto.resquestDto.city.CityRequest;
import com.dto.resquestDto.population.FilterPopulation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpClient {


    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public Response post(Map<String, String> headerList, String jsonPayload, String url) throws IOException {
        log.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, jsonPayload, url);

        Request request = new Request.Builder().post(
                RequestBody.create(jsonPayload, MediaType.parse("application/json"))
        ).headers(Headers.of(headerList)).url(url).build();

        return okHttpClient.newCall(request).execute();
    }

    public Response postForm(Map<String, String> headerList, Map<String, String>  form, FilterPopulation filterPopulation, String url) throws IOException {
        log.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, form, url);
       final FormBody.Builder builder = new FormBody.Builder().add("country", filterPopulation.getCountry())
               .add("limit", String.valueOf(filterPopulation.getLimit())).add("order", filterPopulation.getOrder())
               .add("orderBy", filterPopulation.getOrderBy());

        return getResponse(headerList, form, url, builder);
    }

    public Response postForm(Map<String, String> headerList, Map<String, String>  form, String param, String url) throws IOException {
        log.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, form, url);
        final FormBody.Builder builder = new FormBody.Builder().add("country",param);

        return getResponse(headerList, form, url, builder);
    }

    @NotNull
    private Response getResponse(Map<String, String> headerList, Map<String, String> form, String url, FormBody.Builder builder) throws IOException {
        form.forEach(builder::addEncoded);

        Request request = new Request.Builder().post(builder.build())
                .headers(Headers.of(headerList)).url(url).build();
        log.info("--> Request :: {}", request);
        log.info("--> Request Body :: {}", request.body());
        return okHttpClient.newCall(request).execute();
    }

    public Response postForm(Map<String, String> headerList, Map<String, String>  form, CityRequest cityRequest, String url) throws IOException {
        log.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, form, url);
        final FormBody.Builder builder = new FormBody.Builder().add("country",cityRequest.getCountry()).add("state", cityRequest.getState());

        return getResponse(headerList, form, url, builder);
    }

    @SneakyThrows
    public <T> T post(Map<String, String> headerList, String jsonPayload, String url, Class<T> t) {
        return responseToObject(post(headerList, jsonPayload, url), t);
    }

    private <T> T responseToObject(Response r, Class<T> t) {

        try (r) {
            assert r.body() != null;
            return toPojo(r.body().string(), t);
        } catch (Exception e) {
            log.info("--> Error converting response to object :: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public Response get(Map<String, String> headerList, Map<String, Object> params, String url) throws IOException {
        log.info("Making GET request with header {}, params {} and url {}", headerList, params, url);
        String queryString = params.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        String fullUrl = url + "?" + queryString;
        URL httpUrl = new URL(fullUrl);

        Request request = new Request.Builder().get().headers(Headers.of(headerList)).url(httpUrl).build();
        return okHttpClient.newCall(request).execute();
    }

    @SneakyThrows
    public <T> T get(Map<String, String> headerList, Map<String, Object> params, String url, Class<T> t) {
        return responseToObject(get(headerList, params, url), t);
    }

    public <T> T toPojo(final String o, Class<T> type) {
        try {
            return objectMapper.readValue(o, type);

        } catch (Exception e) {
            log.error("--> conversion of json  to object error {} ", e.getMessage());
            throw new RuntimeException("Error while parsing response");
        }
    }
    public <T> T toPojo(final String o, TypeReference<T> type) {
        try {
            return  objectMapper.readValue(o, type);

        } catch (Exception e) {
            log.error("--> conversion of json  to object error {} ", e.getMessage());
            throw new RuntimeException("Error while parsing response");
        }
    }

    public String toJson(Object src) {
        try {
            return objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.error("conversion to json string error :: {}", e.getMessage());
            return "{}";
        }
    }
}

