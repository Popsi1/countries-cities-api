package com.klasha.service.capital;

import com.klasha.dto.responseDto.capital.CapitalResponse;
import com.klasha.dto.resquestDto.FilterCountry;

public interface CapitalService {
    public CapitalResponse getCapitalCity(FilterCountry filterCountry);
}
