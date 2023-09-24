package com.klasha.service.city;

import com.klasha.dto.responseDto.HttpBaseResponse;
import com.klasha.dto.resquestDto.city.CityRequest;

import java.util.List;

public interface CityService {
    public HttpBaseResponse<List<String>> getCities(CityRequest cityRequest);
}
