package com.service.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dto.resquestDto.FilterCountry;

public interface LocationService {
    public String getLocation(FilterCountry filterCountry) throws JsonProcessingException;
}
