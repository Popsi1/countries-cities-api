package com.service.capital;

import com.dto.responseDto.capital.CapitalResponse;
import com.dto.resquestDto.FilterCountry;

public interface CapitalService {
    public CapitalResponse getCapitalCity(FilterCountry filterCountry);
}
