package com.klasha.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyConversionResponse {
    private String targetCurrency;
    private double amountConverted;
}
