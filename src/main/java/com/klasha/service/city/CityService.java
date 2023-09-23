package com.klasha.service.city;

import com.klasha.dto.responseDto.BaseResponse;
import com.klasha.dto.resquestDto.city.CityRequest;

import java.util.List;

public interface CityService {
    public BaseResponse<List<String>> getCities(CityRequest cityRequest);
}
