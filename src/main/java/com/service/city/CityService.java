package com.service.city;

import com.dto.responseDto.HttpBaseResponse;
import com.dto.resquestDto.city.CityRequest;

import java.util.List;

public interface CityService {
    public HttpBaseResponse<List<String>> getCities(CityRequest cityRequest);
}
