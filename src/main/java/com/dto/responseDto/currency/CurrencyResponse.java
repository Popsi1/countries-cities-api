package com.dto.responseDto.currency;

import lombok.Data;

@Data
public class CurrencyResponse {
    private String name;
    private String currency;
    private String iso2;
    private String iso3;
}
