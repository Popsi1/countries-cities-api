package com.klasha.service.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.klasha.dto.resquestDto.FilterCountry;

public interface LocationService {
    public String getLocation(FilterCountry filterCountry) throws JsonProcessingException;
}
