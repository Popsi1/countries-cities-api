package com.service.currency;

import com.dto.resquestDto.FilterCountry;

public interface CurrencyService {
    public String getCurrency(FilterCountry filterCountry);
}
